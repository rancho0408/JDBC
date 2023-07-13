package boardJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class BoardMain {
	// 필드
	public Scanner in = new Scanner(System.in);
	private static BoardMain bm = new BoardMain();
	private static BoardDAO bdao = new BoardDAO(); // BoardDAO 객체 연결
	public Connection conn = null ; // 1단
	
	// 생성자
	public BoardMain() { // 객체 생성할 때 연결하면 된다!
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "usermanager", "manager0408");
			
		} catch (Exception e) { // Exception으로 예외 퉁처
			System.out.println("드라이버/URL/ID/PW 오류");
			e.printStackTrace();
			bdao.Exit(); // 예외 발생시 강제 종료
		} 
	}

	// 메서드
	public static void main(String[] args) {
		// 프로그램 시작 및 메뉴 구현
		// BoardMain bm = new BoardMain();  
		bm.list(); // main -> list -> mainMenu 한 번에 호출이 가능
	} // end of main method

	public void list() { // 메뉴용 메서드 생성
		System.out.println();
		System.out.println("[게시물 목록]");
		System.out.println("--------------------------------------------------");
		System.out.printf("%-6s %-14s %-15s %-40s \n", "no", "writer", "date", "title");
		System.out.println("--------------------------------------------------");
		
		
		try {
			String sql = "select bno, btitle, bcontent, bwriter, bdate from board order by bno desc";
			PreparedStatement pstmt = conn.prepareStatement(sql); // 3단계 -> 쿼리문 생성 및 입력
			ResultSet rs = pstmt.executeQuery(); // 4단계 -> 쿼리문 실행하여 결과를 ResultSet으로 받음(while까지 포함)
			
			while(rs.next()) { // 오늘은 DTO 사용해보기
				BoardDTO bdto = new BoardDTO(); // 보드 객체 생성
				bdto.setBno(rs.getInt("bno")); // ResultSet에 있는 bno값을 bdto에 넣는다
				bdto.setBtitle(rs.getString("btitle"));
				bdto.setBcontent(rs.getString("bcontent"));
				bdto.setBwriter(rs.getString("bwriter"));
				bdto.setBdate(rs.getDate("bdate")); // BoardDTO객체 완성
				System.out.printf("%-6s %-12s %-14s %-40s \n",
								  bdto.getBno(),	// BoardDTO에 있는 필드값을 순서대로 출력
								  bdto.getBwriter(),
								  bdto.getBdate(),
								  bdto.getBtitle()
								  );
			}
			// 5단계
			rs.close();
			pstmt.close();
			
		} catch (SQLException e) {
			System.out.println("3단계, 4단계를 확인하세요.");
			e.printStackTrace();
		}
		mainMenu(); // mainMenu() 호출

	} // end of list()
	
	public void mainMenu() {
		System.out.println();
		System.out.println("--------------------------------------------------");
		System.out.println("메인 메뉴 : 1. Create | 2. Read | 3. Clear | 4. Exit");
		System.out.print("메뉴 선택 : ");
		int menuNo = in.nextInt(); // 메뉴 입력
		in.nextLine();
		System.out.println();
		
		switch(menuNo) {
		case 1 : bdao.Create();
		case 2 : bdao.Read(); // Search 개념
		case 3 : bdao.Clear();
		case 4 : bdao.Exit();
		default : System.out.println("다시 입력하세요.");
		} // end of switch
		
	} // end of mainMenu()

	
} // end of BoardMain class
