import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;


public class EnumTeste {
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public void teste(){
		//av0 = mv.visitAnnotation("Ljavax/persistence/OneToOne;", true);
		{
		//AnnotationVisitor av1 = av0.visitArray("cascade");
		//av1.visitEnum(null, "Ljavax/persistence/CascadeType;", "ALL");
		//av1.visitEnd();
		}
	}
	
	
}
