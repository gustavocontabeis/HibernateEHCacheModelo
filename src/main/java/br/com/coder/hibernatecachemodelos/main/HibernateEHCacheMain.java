package br.com.coder.hibernatecachemodelos.main;

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
		
		Pessoa pessoa = (Pessoa) session.load(Pessoa.class, 1L);
		printData(pessoa, stats, 1);
		
		pessoa = (Pessoa) session.load(Pessoa.class, 1L);
		printData(pessoa, stats, 2);
		
		//clear first level cache, so that second level cache is used
		session.evict(pessoa);
		
		System.out.println("\nEvict");
		
		pessoa = (Pessoa) session.load(Pessoa.class, 1L);
		printData(pessoa, stats, 3);
		
		pessoa = (Pessoa) session.load(Pessoa.class, 3L);
		printData(pessoa, stats, 4);
		
		pessoa = (Pessoa) otherSession.load(Pessoa.class, 1L);
		printData(pessoa, stats, 5);
		
		//Release resources
		transaction.commit();
		otherTransaction.commit();
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
		System.out.println("Fetch Count=" + stats.getEntityFetchCount());
		System.out.println("Cache Hit Count=" + stats.getSecondLevelCacheHitCount());
		System.out.println("Cache Miss Count=" + stats.getSecondLevelCacheMissCount());
		System.out.println("Cache Put Count=" + stats.getSecondLevelCachePutCount());
		System.out.println();
	}

	private static void printData(Pessoa pessoa, Statistics statisticas, int seq) {
		System.out.println(seq + ":: " + pessoa);
		printStats(statisticas, seq);
	}

}
