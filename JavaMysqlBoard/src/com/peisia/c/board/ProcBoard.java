package com.peisia.c.board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
//

public class ProcBoard {
	Connection con = null;
	Statement st = null;
	ResultSet result = null;

	Scanner sc = new Scanner(System.in);

	void run() {
		Disp.showTitle();
		dbInit();

		loop: while (true) {
			dbPostCount();
			Disp.showMainMenu();
			System.out.println("명령입력:");
			String cmd = sc.next();
			switch (cmd) {
			case "1":
				//글 리스트
				System.out.println("==============================");
				System.out.println("============ 글리스트 ===========");
				System.out.println("==============================");
				System.out.println("글번호 / 글제목 / 작성자id / 작성시간");
				try {
					result = st.executeQuery("select*from board");
					while(result.next()){
						String no=result.getString("b_no");
						String title =result.getString("b_title");
						String id =result.getString("b_id");
						String datetime =result.getString("b_datetime");
						System.out.println(no+"/"+title+"/"+id+"/"+datetime);					
					}
				}catch(SQLException e) {
					e.printStackTrace();
				}			
			
				break;
			case "2":
				//글 읽기
				System.out.println("읽을글 번호를 입력해주세요");
				String readNo = sc.next();
				try {
					result= st.executeQuery("select*from board where b_no="+readNo);
				result.next();
				
			String title=result.getString("b_title");
			String content =result.getString("b_text");
			System.out.println("글 제목:"+title);
			System.out.println("글 내용:"+content);
				}catch(SQLException e) {
					e.printStackTrace();
				}
				break;
		
			case "3":
				// 글작성
				System.out.println("글제목을 입력해주세요:");
				String title = sc.nextLine();
				System.out.println("글내용을 입력해주세요:");
				String content = sc.nextLine();
				System.out.println("유저 아이디를 입력해주세요:");
				String id = sc.next();

				try {
					st.executeUpdate("insert into board " + "(b_title,b_id,b_datetime,b_text,b_hit)" + " values ('"
							+ title + "','" + id + "',now(),'" + content + "',0);");
					System.out.println("글 등록완료");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			case "4":
				//글 삭제
				System.out.println("삭제할 글번호를 입력해주세요");
				String delNo = sc.next();
				dbExecuteUpdate("delete from board where b_no="+delNo);
				break;
			case "5":
				System.out.println("수정할 글번호를 입력하세요");
				String editNo = sc.next();
				System.out.println("제목을 입력해주세요");
				String edTitle= sc.next();
				System.out.println("작성자id를 입력해주세요");
				String edId = sc.next();
				System.out.println("글내용을 입력해주세요");
				String edContent= sc.next();
				dbExecuteUpdate("update board set b_title="+edTitle+",b_id="
						+edId+",b_datetime=+now(),b_text="
						+edContent+" where b_no="+editNo);
				break;

			case "0":
				break;
			case "e":
				System.out.println("프로그램 종료");
				break loop;
			}
		}
	}
	// dbExecuteQuery("select * from tottenham_squad where p_number=7");

	private void dbInit() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/my_cat", "root", "root");
			st = con.createStatement();
			// Statement는 정적 SQL문을 실행하고 결과를 반환받기 위한 객체다.
			// Statement하나당 한개의 ResultSet 객체만을 열 수있다.
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	private void dbExecuteQuery(String query) {
		try {
			result = st.executeQuery(query);
			while (result.next()) { // 결과를 하나씩 빼기. 더 이상 없으면(행 수가 끝나면) false 리턴됨.
				String name = result.getString("p_name"); // p_name 필드(열) 의 데이터 꺼내기(1개 꺼낸거에서)
				System.out.println(name);
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
		}
	}

	private void dbExecuteUpdate(String query) {
		try {
			int resultCount = st.executeUpdate(query);
			System.out.println("처리된 행 수:" + resultCount);
		} catch (SQLException e) {
			e.printStackTrace();
//			System.out.println("SQLException: " + e.getMessage());
//			System.out.println("SQLState: " + e.getSQLState());
		}
	}
	private void dbPostCount() {
		try {
			result=st.executeQuery("select count(*)from board");
			result.next();
			String count=result.getString("count(*)");
			System.out.println("글수"+count);
			}catch(SQLException e)
		{
				e.printStackTrace();
		}
	}
}
