package searchengine.model;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "lemma",
        indexes = {@javax.persistence.Index(columnList = "lemma, site_id",
                name = "KEY_lemma_lemma",
                unique = false)})
@Data
public class Lemma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String lemma;
    @Column(nullable = false)
    private int frequency;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_lemma_site"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final Site site = new Site();

    @Transient
    private float weight;
    public Lemma() {
    }

    public Lemma(String lemma, int frequency, int siteId) {
        this.lemma = lemma;
        this.frequency = frequency;
        this.site.setId(siteId);
    }
    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object obj) {
        Lemma l = (Lemma) obj;
        return lemma.equals(l.lemma) && site == l.site;
    }
    public int getSiteId() {
        return this.site.getId();
    }

    public void setSiteId(int siteId) { this.site.setId(siteId); }

    @Override
    public int hashCode() {
        return lemma.hashCode() + site.hashCode();
    }

    @Override
    public String toString() {
        return "id: " + id + "; lemma: " + lemma + "; frequency: " + frequency + "; site: " + site.getName();
    }
}
