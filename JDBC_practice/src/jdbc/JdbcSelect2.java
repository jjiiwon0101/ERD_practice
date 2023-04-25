package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class JdbcSelect2 {

	public static void main(String[] args) {
		/*
		 사용자에게 scanner를 이용하여 job_id를 입력받습니다.
		 입력받은 job_id에 해당하는 사람들의 first_name, salary를
		 콘솔창에 출력해 주세요. (employees 테이블 사용)
		 조회된 내용이 없다면 조회 결과가 없다고 출력해 주세요.
		 */
		Scanner sc = new Scanner(System.in);
		System.out.println("job_id: ");
		String jobid = sc.next();
		
		
		String sql = "SELECT first_name, salary FROM employees WHERE job_id=?";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String uid = "hr";
		String upw = "hr";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; //SELECT문에서만 사용하는 객체.
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, uid, upw);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, jobid);
			//SELECT문은 executeQuery()를 통해 ResultSet 객체를 받아옵니다.
			//rs는 SELECT문의 쿼리 실행 결과 전체를 들고 있습니다.
			rs = pstmt.executeQuery();
			
			/*
			 - SELECT 쿼리문의 실행 결과, 조회할 데이터가 단 한 줄이라도 존재한다면
			 rs 객체의 next() 메서드는 true값을 리턴하면서 해당 행을 지목합니다.
			 그렇기 때문에 rs에게 데이터를 읽어오실 때는 반드시 next()를 먼저 호출해서
			 데이터의 존재 유무부터 물어 보셔야 합니다.
			 next()메서드를 호출하셔야만 행 하나가 지목되면서 데이터를 불러들일 수 있습니다.
			 */
			
			//조회할 데이터 행의 개수가 여러 개라면 반복문을 이용해서 처리하는게 좋겠죠?
			//만약 조회할 데이터가 한 행이라면 굳이 반복문 열 필요 없이 if문으로 처리합니다.
			int count = 0;
			while(rs.next()) {
				/*
				 - SELECT의 실행 결과 컬럼을 읽어오려면
				 rs의 getString(), getInt(), getDouble()...의 
				 메서드를 사용해서 리턴받습니다. (컬럼의 타입에 따라)
				 */
				String name = rs.getString("first_name"); //컬럼명!
				int salary = rs.getInt("salary");
				
				//LocalDateTime -> Timestamp: Timestamp.valueOf(LocalDateTime);
				//Timestamp -> LocalDateTime: Timestamp.toLocalDateTime();
				count++;
				
				System.out.printf("# first_name: %s\n# salary: %s\n", name, salary);
				System
				.out.println("----------------------------------------------------------------");
			}
			if(count == 0) {
				System.out.println("조회된 결과 없음.");
			} else {
				System.out.println("조회된 행의 개수: " + count + "개");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}

}
