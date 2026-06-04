package api.dto;

public record ProjectSummaryDTO(String id, String name, String createdAt, String updatedAt) {
    @Override
    public String toString() {
        return name + " (Zaktualizowano: " + updatedAt + ")";
    }
}