package boardJDBC;

import java.util.Date;

public class BoardDTO {
	// 데이터베이스에 있는 자료를 객체화하여 *데이터 전이를 담당한다. (*데이터 전이 : getters, setters)

	// 필드 (board 테이블에 있는 필드와 일치)
	private int bno ;
	private String btitle ;
	private String bcontent ;
	private String bwriter ;
	private Date bdate ; // java.util.Date 사용해야함
	
	// 생성자
	
	// 메서드
	public int getBno() { return bno; }
	public String getBtitle() { return btitle; }
	public String getBcontent() { return bcontent; }
	public String getBwriter() { return bwriter; }
	public Date getBdate() { return bdate; }
	
	public void setBno(int bno) { this.bno = bno; }
	public void setBtitle(String btitle) { this.btitle = btitle; }
	public void setBcontent(String bcontent) { this.bcontent = bcontent; }
	public void setBwriter(String bwriter) { this.bwriter = bwriter; }
	public void setBdate(Date bdate) { this.bdate = bdate; }
	
}
