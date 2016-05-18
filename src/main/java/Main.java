import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.com.goit.gojava.kickstarter.Category;
import ua.com.goit.gojava.kickstarter.dao.CategoriesDAO;

import java.util.List;

/**
 * Created by ALEX on 17.05.2016.
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        CategoriesDAO categoriesDAO = context.getBean(CategoriesDAO.class);
        List<Category> categories = categoriesDAO.getCategories();
        System.out.println(categories.toString());
    }
}
