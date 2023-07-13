package boardJDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardDAO {
	// JDBC를 담당하여 CRUD를 구현한다.
	BoardMain bm = new BoardMain(); // main 객체 생성
	BoardDTO bdto = new BoardDTO(); // dto 객체 생성
	
	public void Create() {
		System.out.println("[새 게시물 입력]");
		
		System.out.print("제목 : ");
		bdto.setBtitle(bm.in.nextLine()); // BoardMain에 있는 필드 Scanner 사용
		System.out.print("내용 : ");
		bdto.setBcontent(bm.in.nextLine());
		System.out.print("작성자 : ");
		bdto.setBwriter(bm.in.nextLine());
		// 번호(sequence)랑 날짜(sysdate)는 자동으로 생성
		
		// 보조 메뉴
		System.out.println("--------------------------------------------------");
		System.out.println("보조 메뉴 : 1. OK | 2. Cancle ");
		System.out.print("메뉴 선택 : ");
		int subMenuNo = bm.in.nextInt(); // 메뉴값을 정수로 받아 1이면 insert, 2면 저장 안함
		if(subMenuNo == 1) { // 정수는 ==, 문자면 equals
			
			try {
				String sql = "insert into board (bno, btitle, bcontent, bwriter, bdate)"
						+ "values(board_seq.nextval, ?, ?, ?, sysdate)";
				PreparedStatement pstmt = bm.conn.prepareStatement(sql);
				pstmt.setString(1, bdto.getBtitle()); // 15행에서 set으로 밀어넣은걸 get해서 가지고 옴
				pstmt.setString(2, bdto.getBcontent());
				pstmt.setString(3, bdto.getBwriter()); // SQL문 완성
				int n = pstmt.executeUpdate();
				
				if (n>0) {
					System.out.println(n + "개의 게시물이 등록 되었습니다.");
					bm.conn.commit();
				} else {
					System.out.println("등록이 실패했습니다.");
					bm.conn.rollback();
				}
				pstmt.close();
		
			} catch (SQLException e) {
				System.out.println("insert문 오류 발생 : BoardDAO.create()메서드 오류");
				e.printStackTrace();
			}	
		}
		
		bm.list(); 
	}
	public void Read() { // 게시물의 번호를 입력했을 때 게시물의 내용을 보여준다. (search)
		System.out.println("[게시물 내용 보기]");
		System.out.print("no : ");
		int bno = bm.in.nextInt();
		
		
		try {
			String sql = "select bno, btitle, bcontent, bwriter, bdate from board where bno = ? ";
			PreparedStatement pstmt = bm.conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			
			ResultSet rs = pstmt.executeQuery(); // 쿼리 실행 후 표 형식으로 받는다
			
			if (rs.next()) {
				
				bdto.setBno(rs.getInt("bno"));
				bdto.setBtitle(rs.getString("btitle"));
				bdto.setBcontent(rs.getString("bcontent"));
				bdto.setBwriter(rs.getString("bwriter"));
				bdto.setBdate(rs.getDate("bdate"));
				System.out.println("--------------------------------------------------");
				System.out.println("번호  : " + bdto.getBno());
				System.out.println("제목  : " + bdto.getBtitle());
				System.out.println("내용  : " + bdto.getBcontent());
				System.out.println("작성자 : " + bdto.getBwriter());
				System.out.println("작성일 : " + bdto.getBdate());
				System.out.println("--------------------------------------------------");
				
				// 보조 메뉴 (게시물이 존재할 때 필요하니까 여기에 넣어야 한다.)
				System.out.println("--------------------------------------------------");
				System.out.println("보조메뉴 : 1. Update | 2. Delete | 3. List ");
				System.out.print("메뉴 선택 : ");
				int subMenuNo = bm.in.nextInt();
				bm.in.nextLine();
				if (subMenuNo == 1) {
					Update(bdto); // 데이터 수정용 메서드 호출(dto 객체 전달, 객체 안에는 필드값들이 들어있으니까 객체를 보내는 게 편함?)
				} else if (subMenuNo == 2) {
					Delete(bdto); // 데이터 삭제용 메서드 호출(dto 객체 전달)
				} // List는 어차피 bm.list()가 있으니까 안만들어도 된다.
				
				
				
			} 
			else {
				System.out.println("해당하는 게시물이 존재하지 않습니다.");
			} // if문 종료
			
			rs.close();
			pstmt.close();
			
		} catch (SQLException e) {
			System.out.println("read문 오류 발생 : BoardDAO.read()메서드 오류");
			e.printStackTrace();
		}
		
		bm.list();
	} // end of read()
	private void Delete(BoardDTO bdto) {
		// 게시물을 삭제하고 싶을 때 호출하는 메서드
		// 삭제할 객체를 받아 번호를 이용하여 delete 한다.
		
		try {
			PreparedStatement pstmt = bm.conn.prepareStatement("delete from board where bno =?");
			pstmt.setInt(1,  bdto.getBno());
			int n = pstmt.executeUpdate();
			
			if (n>0) {
				System.out.println(bdto.getBno() + "번의 게시물이 삭제 되었습니다.");
				bm.conn.commit();
			} else {
				System.out.println("삭제가 실패했습니다.");
				bm.conn.rollback();
			}
			pstmt.close();
			
		} catch (SQLException e) {
			System.out.println("delete문 오류 발생 : BoardDAO.Delete()메서드 오류");
			e.printStackTrace();
			Exit();
		}
		
		bm.list();
		
	} // end of delete() method
	private void Update(BoardDTO bdto) {
		// 게시물 내용을 보다가 수정이 필요할 때 호출하는 메서드
		// 수정할 내용을 입력
		System.out.println("[수정할 내용을 입력]");
		System.out.print("제목 : ");
		bdto.setBtitle(bm.in.nextLine()); // 키보드로 입력된 값이 bdto 객체의 제목으로 변경된다.
		System.out.print("내용 : ");
		bdto.setBcontent(bm.in.nextLine());
		System.out.print("작성자 : ");
		bdto.setBwriter(bm.in.nextLine());
		// 날짜는 바꾸면 안된다. (나중에 작성일, 수정일 이런식으로 필드값 늘려서 변경하기 !)
		
		// 보조 메뉴
		System.out.println("--------------------------------------------------");
		System.out.println("보조 메뉴 : 1. OK | 2. Cancle ");
		System.out.print("메뉴 선택 : ");
		int ssubMenuNo = bm.in.nextInt(); // Read() - Update() 인데 Read에서 subMenuNo 변수명을 이미 사용해서 ssubMenuNo
		bm.in.nextLine();
		if(ssubMenuNo == 1) {
			// 수정 작업
			
			try {
				String sql = "update board set btitle = ?, bcontent = ?, bwriter = ? where bno = ?";
				PreparedStatement pstmt = bm.conn.prepareStatement(sql);
				pstmt.setString(1, bdto.getBtitle());
				pstmt.setString(2, bdto.getBcontent());
				pstmt.setString(3, bdto.getBwriter());
				pstmt.setInt(4, bdto.getBno()); // sql문 완성
				
				int n = pstmt.executeUpdate(); // 실행 후 결과를 int로 보낸다
				
				if (n>0) {
					System.out.println(n + "개의 데이터가 수정 되었습니다.");
					bm.conn.commit();
				} else {
					System.out.println("수정이 되지 않았습니다.");
					bm.conn.rollback();
				}
				pstmt.close();
				
			} catch (SQLException e) {
				System.out.println("update문 오류 발생 : BoardDAO.Update()메서드 오류");
				e.printStackTrace();
			} // 예외 처리 종료
			
		} // 메뉴 선택 if문 종료
	} // end of Update() method
	public void Clear() {
		System.out.println("[모든 게시물을 삭제하시겠습니까?]");
		System.out.println("--------------------------------------------------");
		System.out.println("보조 메뉴 : 1. OK | 2. Cancle ");
		System.out.print("메뉴 선택 : ");
		String subMenuNo = bm.in.nextLine(); // Int로 받았을 경우 생기는 오류가 있어서 이번엔 String으로 받아보자.
		
		if(subMenuNo.equals("1")) { // 문자로 받았기 때문에 "" 을 꼭 해줘야 한다. 
			System.out.println("[정말 모든 게시물을 삭제합니까?]");
			System.out.println("--------------------------------------------------");
			System.out.println("보조 메뉴 : 1. OK | 2. Cancle ");
			System.out.print("메뉴 선택 : ");
			subMenuNo = bm.in.nextLine();
			if(subMenuNo.equals("1")) {
				// 한 번 더 확인 후 전체 삭제를 진행
				
				try {
					PreparedStatement pstmt = bm.conn.prepareStatement("truncate table board");
					// 테이블에 있는 모든 자료를 삭제한다. (테이블은 남아있다, 관리자용임으로 사용 시 주의할 것!!)
					pstmt.executeUpdate();
					pstmt.close();
					System.out.println("모든 자료가 지워졌습니다.");
					bm.conn.commit();
					
				} catch (SQLException e) {
					System.out.println("truncate문 오류 발생 : BoardDAO.Clear()메서드 오류");
					e.printStackTrace();
					Exit();
				} 
			} else {
				System.out.println("삭제가 진행되지 않았습니다.");
			} // 한번 더 묻는 if문 종료
			
			} else {
				System.out.println("삭제가 진행되지 않았습니다.");
			} // if문 종
			
		bm.list();
	} // end of Clear() method
	
	public void Exit() {
		if(bm.conn != null) {
			try {
				bm.conn.close();
			} catch (SQLException e) {
				System.out.println("exit문 오류 발생 : BoardDAO.Exit()메서드 오류");
				e.printStackTrace();
			} // 예외처리 종료
		} // if문 종료
		System.out.println("[게시판이 정상적으로 종료 되었습니다.]");
		System.exit(0); // 프로그램 강제 종료
	} // end of exit() method

	
}
