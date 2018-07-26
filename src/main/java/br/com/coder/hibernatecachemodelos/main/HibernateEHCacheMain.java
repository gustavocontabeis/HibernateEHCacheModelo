package br.com.coder.hibernatecachemodelos.main;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;

import br.com.coder.hibernatecachemodelos.model.Endereco;
import br.com.coder.hibernatecachemodelos.model.Pessoa;
import br.com.coder.hibernatecachemodelos.util.HibernateUtil;

public class HibernateEHCacheMain {

	public static void main(String[] args) {
		
		System.out.println("Temp Dir:"+System.getProperty("java.io.tmpdir"));
		
		gerarDados(1L, "Gustavo", 10000.0, "Av. Andarai", "91350-110", "Porto Alegre");
		gerarDados(2L, "Lautenir", 11000.0, "Av. Andarai", "91350-110", "Porto Alegre");
		gerarDados(3L, "Romilda", 12000.0, "Av. Andarai", "91350-110", "Porto Alegre");
		
		//Initialize Sessions
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Statistics stats = sessionFactory.getStatistics();
		System.out.println("Stats enabled="+stats.isStatisticsEnabled());
		stats.setStatisticsEnabled(true);
		System.out.println("Stats enabled="+stats.isStatisticsEnabled());
		
		Session session = sessionFactory.openSession();
		Session otherSession = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Transaction otherTransaction = otherSession.beginTransaction();
		
		printStats(stats, 0);
		
		//Executa query
		Pessoa pessoa = (Pessoa) session.load(Pessoa.class, 1L);
		printData(pessoa, stats, 1);
		
		//Não executa query
		pessoa = (Pessoa) session.load(Pessoa.class, 1L);
		printData(pessoa, stats, 2);
		
		//clear first level cache, so that second level cache is used
		//Limpa o cache de primeiro nível. Agora é o cache de segundo nível que é utilizado.
		session.evict(pessoa);
		
		System.out.println("\nEvict");
		
		//Executa query pois o cache foi limpo
		pessoa = (Pessoa) session.load(Pessoa.class, 1L);
		printData(pessoa, stats, 3);
		
		//Executa query pois o 3 não foi carregado.
		pessoa = (Pessoa) session.load(Pessoa.class, 3L);
		printData(pessoa, stats, 4);
		
		//Executa query pois é outra sessão
		pessoa = (Pessoa) otherSession.load(Pessoa.class, 1L);
		printData(pessoa, stats, 5);
		
		//Não executa query
		pessoa = (Pessoa) session.load(Pessoa.class, 1L);
		printData(pessoa, stats, 6);
		
		//Release resources
		transaction.commit();
		otherTransaction.commit();
		
		/* Testa updates */
		
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		
		pessoa = (Pessoa) session.load(Pessoa.class, 1L);
		pessoa.setNome("Gustavo-2");
		session.update(pessoa);
		transaction.commit();
		
		//Não faz select. Pega do cache
		pessoa = (Pessoa) session.load(Pessoa.class, 1L);
		printData(pessoa, stats, 7);

		//Não deve fazer select
		pessoa = (Pessoa) session.load(Pessoa.class, 1L);
		printData(pessoa, stats, 8);
		
		//Aqui é para fazer query
		List<Pessoa> list = session.createCriteria(Pessoa.class).list();
		
		for (Pessoa object : list) {
			/* O load não deve fazer query pois a query acima carregou todos para o cache */
			Pessoa load = (Pessoa) session.load(Pessoa.class, object.getId());
			System.out.println(load);
		}

		sessionFactory.close();
	}
	
	/**
	 * 
	 * @param idPessoa
	 * @param nomePessoa
	 * @param salario
	 * @param rua
	 * @param cep
	 * @param cidade
	 */
	private static void gerarDados(long idPessoa, String nomePessoa, Double salario, String rua, String cep, String cidade) {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Pessoa pessoa = new Pessoa(idPessoa, nomePessoa, salario, null);
		Endereco endereco = new Endereco(null, rua, cep, cidade, pessoa);
		pessoa.setEndereco(endereco);
		session.save(pessoa);
		session.save(endereco);
		transaction.commit();
		session.close();
	}

	private static void printStats(Statistics stats, int i) {
		System.out.println("***** " + i + " *****");
		System.out.println("Fetch Count=" + stats.getEntityFetchCount());//Get global number of entity fetchs
		System.out.println("Cache Hit Count=" + stats.getSecondLevelCacheHitCount()); //Global number of cacheable entities/collections successfully retrieved from the cache
		System.out.println("Cache Miss Count=" + stats.getSecondLevelCacheMissCount());// Global number of cacheable entities/collections not found in the cache and loaded from the database.
		System.out.println("Cache Put Count=" + stats.getSecondLevelCachePutCount());//Global number of cacheable entities/collections put in the cache
		System.out.println();
	}

	private static void printData(Pessoa pessoa, Statistics statisticas, int seq) {
		System.out.println(seq + ":: " + pessoa);
		printStats(statisticas, seq);
	}

}
