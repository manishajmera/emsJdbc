package com.girnarsoft.employees;

import java.sql.Connection;
import java.util.Scanner;

/**
 * hr services includes add employee,promote employee change employee manager
 * 
 * @author gspl
 *
 */
public class HrServices implements Services {
	Scanner sc = new Scanner(System.in);
	Checkers checkers = new Checkers();
	public static DataBaseOperations dbOperation = new DataBaseOperations();

	/*
	 * add employee into stored Data
	 */
	public int addEmployee(Connection conn, String empDesignation) {
		String empName, empPassword;// emploee name and password
		int mentor, salary;
		int roleId;
		System.out.println("Enter employee name to be added");
		empName = sc.nextLine();
		// validate employee name
		empName = checkers.validateString(empName);
		System.out.println("Enter password of employee");
		empPassword = sc.nextLine();
		// chcek that password can't be empty
		while (empPassword.isEmpty()) {
			System.out.println("Password can't be blank");
			System.out.println("Enter password of employee");
			empPassword = sc.nextLine();

		}
		System.out.println("Enter superviser id");
		// validate mentor id
		mentor = Integer.parseInt(checkers.validateInt(sc.nextLine()));
		// check that if new joinee is trainee then it can't be supervised by trainee
		// and so on for manager and director so that mentor shoyld be at higher
		// position then new joinee
		if (empDesignation.equals("Trainee")) {
			while (!dbOperation.contains(conn, mentor) || dbOperation.getDesignation(mentor, conn).equals("Trainee")) {
				System.out.println("Mentor not exsit in dataBase oryou can't assign it! try again");
				mentor = Integer.parseInt(checkers.validateInt(sc.nextLine()));

			}
		} else if (empDesignation.equals("Manager")) {
			while (!dbOperation.contains(conn, mentor) || dbOperation.getDesignation(mentor, conn).equals("Trainee")
					|| dbOperation.getDesignation(mentor, conn).equals("Manager")) {
				System.out.println("Mentor not exsit in dataBase or you can't assign it! try again");
				mentor = Integer.parseInt(checkers.validateInt(sc.nextLine()));
			}

		} else if (empDesignation.equals("Director")) {

			while (!dbOperation.contains(conn, mentor) || dbOperation.getDesignation(mentor, conn).equals("Trainee")
					|| dbOperation.getDesignation(mentor, conn).equals("Manager")
					|| dbOperation.getDesignation(mentor, conn).equals("Director")) {
				System.out.println("Mentor not exsit in dataBase or you can't assign it! try again");
				mentor = Integer.parseInt(checkers.validateInt(sc.nextLine()));
			}

		}
		// through roleId find his designation through database
		roleId = dbOperation.getRoleId(empDesignation, conn);
		System.out.println("Enter salary of employee");
		salary = Integer.parseInt(checkers.validateInt(sc.nextLine()));
		int empId = dbOperation.insertEmp(conn, empName, empPassword, roleId, mentor, salary);
		return empId;
	}

	/*
	 * promote employees from trainee to manager are in this method
	 */
	public void promote(Connection conn) {
		System.out.println("Enter employee id you want to promote");
		int empId = Integer.parseInt(checkers.validateInt(sc.nextLine()));
		while (!dbOperation.contains(conn, empId)) {
			System.out.println("Please enter valid id");
			empId = Integer.parseInt(checkers.validateInt(sc.nextLine()));

		}
		if (dbOperation.getDesignation(empId, conn).equals("Director")
				|| dbOperation.getDesignation(empId, conn).equals("HR")
				|| dbOperation.getDesignation(empId, conn).equals("CEO")) {
			System.out.println(
					"please type correct id or this employee is already at higher position or at same position that u want to promote.");
		} else {
			dbOperation.promote(empId, conn);
		}

	}

	/*
	 * Delete employee from database are in this method
	 */
	public void deleteEmployee(Connection conn, int empId) {
		dbOperation.delete(conn, empId);
	}

	/*
	 * Change supervisor of employee through his id and mentor id you u want ot
	 * change
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.girnarsoft.employees.Services#changeSupervisor(java.sql.Connection)
	 */
	public void changeSupervisor(Connection conn) {
		System.out.println("Enter employee id who you want to change the supervisor");
		int empId = Integer.parseInt(checkers.validateInt(sc.nextLine()));
		System.out.println("Enter new supervisor id of this employee");
		while (!dbOperation.contains(conn, empId)) {
			System.out.println("Please enter valid id");
			empId = Integer.parseInt(checkers.validateInt(sc.nextLine()));

		}
		int mentor = Integer.parseInt(checkers.validateInt(sc.nextLine()));
		while (!dbOperation.contains(conn, mentor)) {
			System.out.println("Please enter valid id");
			mentor = Integer.parseInt(checkers.validateInt(sc.nextLine()));

		}
		dbOperation.changeSupervisor(conn, empId, mentor);
	}
}
