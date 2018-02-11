package pl.ute.culturaltip.api.wikipedia.extracts;

/**
 * Created by dominik on 11.02.18.
 */

public class ExtractsArticlePage {
    private int pageid;
    private int ns;
    private String title;
    private String extract;

    public int getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public int getNs() {
        return ns;
    }

    public void setNs(int ns) {
        this.ns = ns;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }
}
