package ua.com.goit.gojava.kickstarter;

/**
 * Created by alex on 25.12.15.
 */
public class Category {

    private String name;
    private int id;

    @Deprecated
    public Category(String name) {
        this.name=name;
    }

    public Category(int id, String name) {
        this(name);
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return !(name != null ? !name.equals(category.name) : category.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return String.format
                //("Category [name=%s, id=%s]" ,name, id);
                ("%s %s",id,name);
    }
}
