package com.girnarsoft.employees;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Scanner;
/**
 * I have created one ceo and one hr who has all the rights to add employee
 * promote,and delete from database 
 * this class shows the user menu and his menu actions
 * in this class jdbc data connection is established through resource bundle
 * @author gspl
 *
 */

public class EmsDemo {
	//final scanner to take users input
	private static final Scanner SC = new Scanner(System.in);
	//variable to store newly created employee id
	public static int newlyCreatedEmpId;
	//object of checker class which validate the valid inputs
	public static Checkers checkers = new Checkers();
	//database operation object for change in database and fetch from database
	public static DataBaseOperations dbOperation = new DataBaseOperations();
	//JDBC_Driver 
	public static String JDBC_DRIVER;
	//database path
	public static String TARGET_DB_URL;
	//database username 
	public static String USER;
	//database password
	public static String PASS;
	//database connection object
	public static Connection CONN = null;
	//resource bundle to fetch constant data of database
	public static ResourceBundle bundle = null;
	/*
	 * Initialization of database
	 */
	static {

		bundle = ResourceBundle.getBundle("db");
		JDBC_DRIVER = bundle.getString("JDBC_DRIVER");
		TARGET_DB_URL = bundle.getString("TARGET_DB_URL");
		USER = bundle.getString("USER");
		PASS = bundle.getString("PASS");

		try {

			CONN = getDbTxConnection(TARGET_DB_URL, USER, PASS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * creation of database connection
	 */
	public static Connection getDbTxConnection(String dbUrl, String username, String password) {
		Connection conn = null;
		try {
			
			Class.forName(JDBC_DRIVER);
			System.out.println("Connecting to database... ");
			conn = DriverManager.getConnection(dbUrl, username, password);
			
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	/*
	 * main menu functions for Hr Ceo Manager Trainee Director
	 */
	public static void main(String[] args) throws SQLException {
		//after login we got all the funtionalities of that correponding roles
		String choice;
		//password for employee
		String password;
		//pass
		Integer empId;
		try {
			
		while (true) {
			System.out.println("Hello ! Please enter your employee id: ");
			empId = Integer.parseInt(checkers.validateInt(SC.nextLine()));
			//validate users enter a vlid employee id and it should be integer
			while (!dbOperation.contains(CONN, empId)) {
				System.out.println("Employee not exist ! try again");
				System.out.println("Hello ! Please enter your employee id: ");
				empId = Integer.parseInt(checkers.validateInt(SC.nextLine()));

			}
			System.out.println("-----" + empId);
			System.out.println("Enter your Password: ");
			password = SC.nextLine();
			//verify password from database
			while (!dbOperation.verifyPassword(empId, password, CONN)) {
				System.out.println("Please enter correct credentials try again");
				password = SC.nextLine();
			}
			choice = dbOperation.getDesignation(empId, CONN);
			switch (choice) {
			case "CEO":
				actionMenu(empId);
				break;
			case "HR":
				actionMenu(empId);
				break;
			case "Manager":
				System.out.println("#########   Welcome Manager   ###############");

				dbOperation.team(CONN, empId);
				break;
			case "Director":
				System.out.println("#########   Welcome Director   ###############");

				dbOperation.team(CONN, empId);
				break;
			default:
				System.out.println("#########   Welcome Trainee   ###############");

				dbOperation.supervisedBy(CONN, empId);
				break;
			}
		}
		}catch(Exception e){
			e.printStackTrace();
			CONN.rollback();
		}finally{  
			  
			if(CONN!=null){  
				CONN.close();  
			}
		}
	

	}

	/*
	 *  action menu  of hr and ceo
	 */
	
	private static void actionMenu(int empId) {
		int exitFromLoop = 0;
		int choose;
		//check who is logged in through empId
		if (empId == 1) {
			System.out.println("#########   Welcome Ceo   ###############");
		} else {
			System.out.println("########    Welcome Hr ##############S");
		}
		while (exitFromLoop != -1) {
			System.out.println("1.Add employee");
			System.out.println("2.Delete employee");
			System.out.println("3.Show Employees Details");
			System.out.println("4.Promotion");
			System.out.println("5.Show Employees Under Me");
			System.out.println("6.Change Supervisor");
			System.out.println("Press any number other then this for logout");
			choose = Integer.parseInt(checkers.validateInt(SC.nextLine()));
			Services hr = new HrServices();
			switch (choose) {
			case 1:

				while (true) {
					System.out.println("Choose Designation");
					System.out.println("1.Add Trainee \t 2. Add Manager \t 3. Add Director");
					int designation = Integer.parseInt(new Checkers().validateInt(SC.nextLine()));

					if (designation == 1) {
						newlyCreatedEmpId = hr.addEmployee(CONN, "Trainee");
						System.out.println("Trainee added successfully ! with employee id: " + newlyCreatedEmpId);
						break;
					} else if (designation == 2) {
						newlyCreatedEmpId = hr.addEmployee(CONN, "Manager");
						System.out.println("Manager added successfully ! with employee id: " + newlyCreatedEmpId);
						break;
					} else if (empId == 1 && designation == 3) {
						newlyCreatedEmpId = hr.addEmployee(CONN, "Director");
						System.out.println("Director added successfully ! with employee id: " + newlyCreatedEmpId);
						break;
					} else {
						System.out.println("XXXXXX Wrong Input! try Sagain");
						continue;
					}
				}
				break;
			case 2:
				System.out.println("Enter employee id you want to remove");
				int employeeRemoveId = Integer.parseInt(checkers.validateInt((SC.nextLine())));
				if (!dbOperation.contains(CONN, employeeRemoveId)) {
					System.out.println("please type correct id");
				} else {
					hr.deleteEmployee(CONN, employeeRemoveId);

				}
				break;
			case 3:
				System.out.println("Employees________");
				System.out.println("");
				dbOperation.showEmployee(CONN);
				break;
			case 4:
				if (empId == 1) {
					Services ceo = new CeoServices();
					ceo.promote(CONN);
				} else
					hr.promote(CONN);
				break;
			case 5:
				dbOperation.team(CONN, empId);
				break;
			case 6:
				hr.changeSupervisor(CONN);
				break;

			default:
				System.out.println("Log Out -----");
				exitFromLoop = -1;
			}
		}
	}
}
