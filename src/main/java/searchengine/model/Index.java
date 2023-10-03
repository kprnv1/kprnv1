package searchengine.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "`index`")
@Data
public class Index {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id", nullable = false, foreignKey = @ForeignKey(name = "FK_index_page"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final Page page = new Page();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lemma_id", nullable = false, foreignKey = @ForeignKey(name = "FK_index_lemma"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final Lemma lemma = new Lemma();
    @Column(name = "`rank`", nullable = false)
    private float rank;

    public Index(int pageId, int lemmaId, float rank) {
        this.page.setId(pageId);
        this.lemma.setId(lemmaId);
        this.rank = rank;
    }

    public int getLemmaId() {
        return this.lemma.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != getClass()) {
            return false;
        }
        Index i = (Index) obj;
        return id == i.id;
    }

    public int getPageId() { return page.getId(); }

    @Override
    public int hashCode() {
        return id + page.hashCode() + lemma.hashCode();
    }

    @Override
    public String toString() {
        return "id: " + id + "; page: " + page.getPath() + "; lemma: " + lemma.getLemma();
    }
}
