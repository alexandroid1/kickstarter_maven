package ua.com.goit.gojava.kickstarter.dao;

import ua.com.goit.gojava.kickstarter.Categories;
import ua.com.goit.gojava.kickstarter.Category;


import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by alex on 27.01.16.
 */
public class CategoriesDAO implements Categories {

    private DataSource dataSource;

    public CategoriesDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(final Category category) {
            String insertTableSQL = "INSERT INTO Categories"
                    + "(name) VALUES"
                    + "(?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = getConnection().prepareStatement(insertTableSQL);
            preparedStatement.setString(1, category.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("something wrong with getting connection: ", e);
        }

    }

    @Override
    public List<Category> getCategories() {
        try {
            Statement statement = getConnection().createStatement();
            statement.setQueryTimeout(30);
            List<Category> result = new LinkedList<Category>();

            ResultSet rs = statement
                    .executeQuery("select * from Categories");
            while (rs.next()) {
              result.add(new Category(rs.getInt("id"), rs.getString("name")));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("something wrong with getting connection: ", e);
        }
    }

    @Override
    public Category get(int index) {
        try {
            Statement statement = getConnection().createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement
                    .executeQuery("select * from Categories WHERE id-1 = " + index);
            while (rs.next()) {
                return new Category(rs.getInt("id"), rs.getString("name"));
            }

            throw new RuntimeException("Category not found");
        } catch (SQLException e) {
            throw new RuntimeException("something wrong with getting connection: ", e);
        }
    }

    @Override
    public int size() {
        try {
            Statement statement = getConnection().createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("select COUNT(*) FROM Categories");
            return  rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("something wrong with getting connection: ", e);
        }
    }

    @Override
    public boolean exists(final int id) {
        try {
            Statement statement = getConnection().createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("select id FROM Categories WHERE id = " + id);
            return  rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("something wrong with getting connection: ", e);
        }
    }

    public Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }
}
