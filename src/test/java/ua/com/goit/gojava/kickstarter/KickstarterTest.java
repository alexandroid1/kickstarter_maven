package ua.com.goit.gojava.kickstarter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by alex on 12.01.16.
 */
public class KickstarterTest {

    public static final String CATEGORIES_FILE = "categories.txt";
    private QuoteGenerator generator;
    private IO io;
    private Categories categories;
    private Projects projects;
    private Kickstarter kickstarter;

    @After
    public void cleanUp(){
        new File(CATEGORIES_FILE).delete();
        projects = null;
        categories = null;
        io = null;
        generator = null;

    }


    @Before
    public void setup(){
        new File(CATEGORIES_FILE).delete();
        projects = null;
        categories = null;
        io = null;
        generator = null;


        generator = mock(QuoteGenerator.class);
        when(generator.nextQuote()).thenReturn("quote");

        io = mock(IO.class);
        categories = new InFileCategories(CATEGORIES_FILE);
        projects = new Projects();

        kickstarter = new Kickstarter(categories, projects, io, generator);
    }

    @Test
    public void shouldExitFromProgram_WhenExitFromCategoriesMenu(){
        when(io.read()).thenReturn("0");
        kickstarter.run();
    }

    @Test
    public void shouldNoCategoriesInProject_whenSelectCategory(){
        categories.add(new Category("category1"));
        categories.add(new Category("category2"));

        when(io.read()).thenReturn("1", "0", "0");
        kickstarter.run();

        List<String> values = assertPrinted(io, 9);

        assertEquals("[quote\n" +
                ", Choose category or 0 for Exit:\n" +
                ", [1 category1, 2 category2]\n" +
                ", You chosen category:category1\n" +
                ", ----------------\n" +
                ", No any projects in cathegory. Press 0 - for exit \n" +
                ", Choose category or 0 for Exit:\n" +
                ", [1 category1, 2 category2]\n" +
                ", Thanks for using Kickstarter\n" +
                "]", values.toString());
    }

    @Test
    public void shouldMenuWithProject(){
        Category category = new Category("category1");
        categories.add(category);

        Project project1 = new Project("project1", 100, 1000, "video1", "description1");
        projects.add(project1);

        project1.setCategory(category);

        Project project2 = new Project("project2", 200, 2000, "video2", "description2");
        projects.add(project2);

        project2.setHistory("history2");
        project2.addQuestionAnswer("QA");
        project2.setCategory(category);

        when(io.read()).thenReturn("1", "2", "0", "0", "0", "0");
        kickstarter.run();

        List<String> values = assertPrinted(io, 32);

        assertEquals("[quote\n" +
                ", Choose category or 0 for Exit:\n" +
                ", [1 category1]\n" +
                ", You chosen category:category1\n" +
                ", ----------------\n" +
                ", 1 - , project1\n" +
                ", description1\n" +
                ", Need money : 100\n" +
                ", Already have money: 0$\n" +
                ", Days to get money : 1000\n" +
                ", ----------------\n" +
                ", 2 - , project2\n" +
                ", description2\n" +
                ", Need money : 200\n" +
                ", Already have money: 0$\n" +
                ", Days to get money : 2000\n" +
                ", ----------------\n" +
                ", Choose project: [1 ... 2] or 0 for exit \n" +
                ", You chosen project:project2\n" +
                ", ----------------\n" +
                ", project: \n" +
                ", history2\n" +
                ", video2\n" +
                ", QA\n" +
                ", ----------------\n" +
                ", Choose action: \n" +
                "0 - List of projects; 1 - Invest in the project; 2 - Ask authors\n" +
                ", Choose project: [1 ... 2] or 0 for exit \n" +
                ", Choose category or 0 for Exit:\n" +
                ", [1 category1]\n" +
                ", Thanks for using Kickstarter\n" +
                "]", values.toString());
    }

    @Test
    public void shouldPrintProjectMenu_whenSelectIt(){
        Category category = new Category("category1");
        categories.add(category);

        Project project = new Project("project1", 100, 1000, "video1", "description1");
        projects.add(project);

        project.setCategory(category);

        when(generator.nextQuote()).thenReturn("quote");
        when(io.read()).thenReturn("1", "1", "1", "0", "0", "0");

        kickstarter.run();

        List<String> values = assertPrinted(io, 53);
        assertPrinted(values, "Choose action: \n" +
                "0 - List of projects; 1 - Invest in the project; 2 - Ask authors\n");
        assertPrinted(values, "Thank you for what you want to invest in the project\n");
    }

    @Test
    public void shouldSelectCategoryWithoutProjects(){

        categories.add(new Category("category1"));
        categories.add(new Category("category2"));

        when(io.read()).thenReturn("1", "0", "0");

        kickstarter.run();

        List<String> values = assertPrinted(io, 9);
        assertPrinted(values, "quote\n");
        assertPrinted(values, "Choose category or 0 for Exit:\n");
        assertPrinted(values, "[1 category1, 2 category2]\n");
        assertPrinted(values, "You chosen category:category1\n");
        assertPrinted(values, "----------------\n");
        assertPrinted(values, "No any projects in cathegory. Press 0 - for exit \n");
        assertPrinted(values, "Thanks for using Kickstarter\n");
    }

    @Test
    public void shouldIncomeAmountToProject_whenDonate(){
        int TOTAL = 100;
        int DONATE = 25;
        int STILL_NEEDED = 75;

        Category category = new Category("category1");
        categories.add(category);

        Project project = new Project("project1", TOTAL, 1000, "video1", "description1");
        projects.add(project);

        project.setCategory(category);

        when(io.read()).thenReturn("1", "1", "1", "Alex", "231321321", "" + DONATE, "0", "0", "0");

        kickstarter.run();

        List<String> values = assertPrinted(io, 31);
        assertPrinted(values, "Choose action: \n" +
                "0 - List of projects; 1 - Invest in the project; 2 - Ask authors\n");
        assertPrinted(values, "Thank you for what you want to invest in the project\n");
        assertPrinted(values, "enter your name:\n");
        assertPrinted(values, "enter your credit card number:\n");
        assertPrinted(values, "enter the amount of money:\n");
        assertPrinted(values, "Thank you Alex Money amounting to 25 successfully deposited!\n");

        assertEquals(STILL_NEEDED, project.getAmount());

    }

    private void assertPrinted(List<String> values, String expected) {
        assertTrue("Actual data: \n" + values.toString() + "doesn't contain: \n\n" + expected,
                values.contains(expected));
    }

    private List<String> assertPrinted(IO io, int times) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(io, times(times)).print(captor.capture());
        return captor.getAllValues();
    }

    @Test
    public void shouldMyQuestionOnProject_whenAssItOnProjectMenu(){
        Category category = new Category("category1");
        categories.add(category);

        Project project = new Project("project1", 100, 1000, "video1", "description1");
        projects.add(project);

        project.setCategory(category);

        when(io.read()).thenReturn("1", "1", "2", "When are you going to release the film?",
                "0", "0", "0");

        kickstarter.run();

        List<String> values = assertPrinted(io, 27);
        assertPrinted(values, "Choose action: \n" +
                "0 - List of projects; 1 - Invest in the project; 2 - Ask authors\n");
        assertPrinted(values, "Enter your question:\n");
        assertPrinted(values, "Thank you for your question, the authors will soon contact you\n");

        assertEquals("When are you going to release the film?", project.getQuestionAnswers());
    }

    @Test
    public void shouldAddQuestion_whenAskIt(){
        Project project = new Project("project1", 100, 1000, "video1", "description1");
        assertEquals(null, project.getQuestionAnswers());

        project.addQuestionAnswer("question");

        assertEquals("question", project.getQuestionAnswers());
    }

    @Test
    public void shouldAddSecondQuestion_whenAskIt(){
        Project project = new Project("project1", 100, 1000, "video1", "description1");
        project.addQuestionAnswer("question");
        project.addQuestionAnswer("answer");
        assertEquals("question\nanswer", project.getQuestionAnswers());
    }

}
