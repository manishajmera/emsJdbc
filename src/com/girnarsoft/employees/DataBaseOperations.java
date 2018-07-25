package com.girnarsoft.employees;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * all the database operations that we are doing are in this class
 * 
 * @author gspl
 *
 */
public class DataBaseOperations {
	//preparedStatement is use for execute sql queries
	private PreparedStatement pstmt = null;
	//result is for storing result that we are getting from database
	private ResultSet rs = null;
	/*
	 * delete method for deletion of employee and set all his team mentor to be null
	 */
	protected void delete(Connection con, int empId) {

		String sql = "update emp set status = 0 where empId = " + empId;
		String sql2 = "update emp set supervisedBy = NULL where supervisedBY=" + empId;

		try {
			con.setAutoCommit(false);

			pstmt = con.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt = con.prepareStatement(sql2);
			pstmt.executeUpdate();
			con.commit();
		}  catch(Exception e){
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	
/*
 * check that employee exit in database
 */
	protected boolean contains(Connection con, int empId) {
		String sql = "select empId from emp where status=1";
		try {
			con.setAutoCommit(false);

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (empId == rs.getInt(1))
					return true;
			}
			con.commit();

		}  catch(Exception e){
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return false;
	}
	/*
	 * insert employee in the database
	 */

	protected int insertEmp(Connection con, String name, String password, int roleId, int supervisedBy, int salary) {
		String sql = "insert into emp(name,password,roleId,supervisedBy,salary)" + "values('" + name + "','" + password
				+ "'," + roleId + "," + supervisedBy + "," + salary + ");";
		try {
			con.setAutoCommit(false);

			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);
			}
			con.commit();
		} catch(Exception e){
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return 0;

	}
	/*
	 * verify password 
	 */

	protected boolean verifyPassword(int empId, String password, Connection con) {
		String sql = "select empId,password from emp";
		try {
			con.setAutoCommit(false);

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {

				if (empId == rs.getInt(1) && password.equals(rs.getString(2)))
					return true;
			}
			con.commit();

		} catch(Exception e){
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	
		return false;

	}
	/* 
	 * to find the name of designation  
	 * 
	 */

	protected String getDesignation(int empId, Connection con) {
		String designation = null;
		String sql = "select roleName,empId from role inner join emp on emp.roleId = role.roleId;";
		try {
			con.setAutoCommit(false);

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			con.commit();
			while (rs.next()) {
				if (empId == rs.getInt(2)) {
					designation = rs.getString(1);
				}
			}

		}  catch(Exception e){
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return designation;
	}
/*
 * update supervisor if we delete an employee
 */
	protected String updateSupervisor(int empId, Connection conn) {
		String designation = null;
		String sql = "update emp set status=0 where empId = " + empId + ";";

		try {
			conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			conn.commit();
		}  catch(Exception e){
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return designation;
	}
/*
 * to find that role if it exit in database or not
 */
	protected int getRoleId(String empDesignation, Connection con) {
		int designationId = 0;
		String sql = "select roleId from role where roleName = '" + empDesignation + "';";
		try {
			con.setAutoCommit(false);

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				designationId = rs.getInt(1);
			}
			con.commit();

		}  catch(Exception e){
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return designationId;
	}
/*
 * to change the designation of the employee
 */
	protected String promote(int empId, Connection con) {
		String designation = null;
		int roleId = 0;
		String sql = "select roleId from emp where empId = " + empId + ";";
		try {
			con.setAutoCommit(false);

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				roleId = rs.getInt(1);
			}
			roleId++;
			sql = "update emp set roleId=" + roleId + " where empId = " + empId + ";";
			pstmt = con.prepareStatement(sql);
			pstmt.executeUpdate();
			con.commit();

		}  catch(Exception e){
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return designation;
	}
/*
 * display all the employees;
 * 
 */
	protected void showEmployee(Connection con) {

		String sql = "select empId,name,supervisedBy,roleName from emp inner join role on emp.roleId = role.roleId where emp.status;";
		try {
			con.setAutoCommit(false);

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println("empId \t \t Mentor  \t NAME  \t \t roleName");
			while (rs.next()) {
				System.out.println(rs.getInt(1) + " \t \t " + rs.getInt(3) + " \t \t " + rs.getString(2) + " \t \t"
						+ rs.getString(4));
			}
			System.out.println("\n");
			con.commit();

		}  catch(Exception e){
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
/*
 * display team of the employee
 */
	protected void team(Connection con, int empId) {
		String sql = "select empId , name ,roleName from emp inner join role on emp.roleId = role.roleId "
				+ "where supervisedBy =" + empId;
		try {
			con.setAutoCommit(false);

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println("empId \t \t NAME \t \t Designation");
			while (rs.next()) {
				System.out.println(rs.getInt(1) + "\t \t" + rs.getString(2) + "\t \t" + rs.getString(3));
			}
			System.out.println("-------------------------------------");
			con.commit();
		}  catch(Exception e){
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

	}
/*
 * display the supervisor of the employee;
 */
	protected void supervisedBy(Connection con, int empId) {
		String sql = "select empId,name from emp where empId = (select supervisedBy from emp where empId=" + empId
				+ ");";
		try {
			con.setAutoCommit(false);

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				System.out.println(rs.getInt(1) + "\t \t" + rs.getString(2) + "\t \t" + rs.getString(3));
			}
			System.out.println("-------------------------------------SSS");
			con.commit();
		}  catch(Exception e){
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

	}
/*
 * change the supervisor 
 */
	protected void changeSupervisor(Connection con, int empId, int mentor) {
		String sql = "update emp set supervisedBy=" + mentor + " where empId = " + empId + ";";

		try {
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(sql);
			pstmt.executeUpdate();
			con.commit();
		} catch(Exception e){
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

}