package searchengine.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "lemma_all",
        indexes = {@javax.persistence.Index(columnList = "lemma, site_id, page_id",
                name = "KEY_lemma_all",
                unique = false)})
@Data
public class LemmaAll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String lemma;
    @Column(nullable = false)
    private int frequency;
    @Column(name = "site_id", nullable = false)
    private int siteId;
    @Column(name = "page_id", nullable = false)
    private int pageId;

    public LemmaAll() {
    }

    public LemmaAll(String lemma, int frequency, int siteId, int pageId) {
        this.lemma = lemma;
        this.frequency = frequency;
        this.siteId = siteId;
        this.pageId = pageId;
    }

    @Override
    public int hashCode() {
        return lemma.hashCode() + siteId;
    }

    @Override
    public String toString() {
        return "id: " + id + "; lemma: " + lemma + "; frequency: " + frequency + "; site: " + siteId + "; page: " + pageId;
    }
}
