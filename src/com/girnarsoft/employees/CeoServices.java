package com.girnarsoft.employees;

import java.sql.Connection;
import java.util.Scanner;

/**
 * this method extends hr so all the funtionalities of hr are inherited here
 * and promotion funtionalitie is override so ceo can promote manager to director
 */
public class CeoServices extends HrServices implements Services {
	Scanner sc = new Scanner(System.in);
	Checkers checkers = new Checkers();
	DataBaseOperations dbOperations = new DataBaseOperations(); 

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.girnarsoft.employees.HrServices#promote(java.util.Map) promotion
	 * method
	 * promotion method of ceo  where ceo can promote employee to manager,director
	 */
	public void promote(Connection conn) {
		System.out.println("Enter employee id you want to promote");
		int empId = Integer.parseInt(checkers.validateInt(sc.nextLine()));
		//check that employee id should be valid
		while (!dbOperation.contains(conn,empId)) {
			System.out.println("Please enter valid id");
			empId = Integer.parseInt(checkers.validateInt(sc.nextLine()));
		}
		//promotiom from trainee to manager
		if (dbOperation.getDesignation(empId, conn).equals("Trainee")) {
			System.out.println("Do you want to promote this employee type Y/N");
			String check = sc.nextLine();
			while (!check.equals("Y")) {
				System.out.println("please type valid input");
				check = sc.nextLine();
			}
			if (check.equals("Y")) {
				//changes made in database
				dbOperation.promote(empId,conn);

			}
	//promotion of manager to director
		}	else if (dbOperation.getDesignation(empId, conn).equals("Manager")) {
			System.out.println("Do you want to promote this employee type Y/N");
			String check = sc.nextLine();
			while (!check.equals("Y")) {
				System.out.println("please type valid input");
				check = sc.nextLine();
			}
			if (check.equals("Y")) {
				dbOperation.promote(empId, conn);
			}
		} else {
			System.out.println("please type valid input id or employee is already at higher position");
			promote(conn);
		}
	}
	

}
