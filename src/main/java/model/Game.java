package model;

public class Game {
    private int id;
    private String name;
    private String released;
    private int metacritic;
    private String description_raw;
    private String background_image;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public int getMetacritic() {
        return metacritic;
    }

    public void setMetacritic(int metacritic) {
        this.metacritic = metacritic;
    }

    public String getDescription_raw() {
        return description_raw;
    }

    public void setDescription_raw(String description_raw) {
        this.description_raw = description_raw;
    }

    public String getBackground_image() {
        return background_image;
    }

    public void setBackground_image(String background_image) {
        this.background_image = background_image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSummary() {
        return "ID: " + id + " | Status: " + status + " | Nome: " + name;
    }

    @Override
    public String toString() {
        return String.format(
                "----------------------------------------\n" +
                        " ID: %d | %s\n" +
                        "----------------------------------------\n" +
                        " Status: %s\n" +
                        " Lançamento: %s\n" +
                        " Nota Metacritic: %d\n" +
                        " Link da Imagem: %s\n" +
                        " ------------------\n" +
                        " Descrição:\n %s\n" +
                        "----------------------------------------",
                id, name, status, released, metacritic, background_image, description_raw
        );
    }
}


