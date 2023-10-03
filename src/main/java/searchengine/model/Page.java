package searchengine.model;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "page",
        indexes = {@javax.persistence.Index(columnList = "path",
                name = "KEY_page_path")})
@Data
public class Page implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false, foreignKey = @ForeignKey(name = "FK_page_site"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final Site site = new Site();
    @Column(columnDefinition = "varchar(512)", nullable = false)
    private String path;
    @Column(nullable = false)
    private int code;
    @Column(columnDefinition = "mediumtext", nullable = false)
    private String content;

    @Transient
    private String title;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSiteId(int siteId) { this.site.setId(siteId); }
    public int getSiteId() { return this.site.getId(); }

    @Override
    public int hashCode() {
        return path != null && site.getId() != 0 ? path.hashCode() + site.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        Page p = (Page) obj;
        return site.getId() == 0 ||
                getClass() == obj.getClass() && path.equals(p.path) && site == p.site;
    }

    @Override
    public String toString() {
        return "id: " + id + ", siteId: " + site.getId() + ", path: " + path;
    }
}
