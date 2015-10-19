import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DAO {
	private static DAO dao;
	private Configuration configuration;

	public DAO(String dbType, String dbPathName, String user, String name) {
		if(dbType.equals("hsqldb")){
			configuration = new Configuration().
					setProperty("hibernate.dialect","org.hibernate.dialect.HSQLDialect").
					setProperty("hibernate.connection.driver_class","org.hsqldb.jdbcDriver").
					//setProperty("hibernate.connection.url","jdbc:hsqldb:file:C:/bancos/person_db;hsqldb.lock_file=false").
					setProperty("hibernate.connection.url","jdbc:hsqldb:file:" + dbPathName + ";readonly=false").
					setProperty("hibernate.connection.username",user).
					setProperty("hibernate.connection.password",name).				
					setProperty("hibernate.show_sql","true").
					setProperty("hibernate.format_sql","true").
					setProperty("hibernate.hbm2ddl.auto","update");
		}else if(dbType.equals("postgresql")){
			configuration = new Configuration().
					setProperty("hibernate.dialect","org.hibernate.dialect.PostgreSQLDialect").
					setProperty("hibernate.connection.driver_class","org.postgresql.Driver").
					setProperty("hibernate.connection.url","jdbc:postgresql://localhost:5432/jpa_hibernate").
					setProperty("hibernate.connection.username","postgres").
					setProperty("hibernate.connection.password","unip").				
					setProperty("hibernate.show_sql","true").
					setProperty("hibernate.format_sql","true").
					setProperty("hibernate.hbm2ddl.auto","update");
		}
	}

	public static DAO getInstance(String dbType, String dbPathName, String user, String name){
		if(dao != null){
			return dao;
		}
		return new DAO(dbType, dbPathName, user, name);
	}
	
	public void addAnnotedClassToConnection(Class clazz){
		configuration.addAnnotatedClass(clazz);
	}
	
	public Session getSession(){
		   SessionFactory sessionFactory = configuration.buildSessionFactory();
		   return sessionFactory.openSession();
	}
}