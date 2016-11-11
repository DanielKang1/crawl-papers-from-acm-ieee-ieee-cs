package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by D.niel_K on 16/11/8.
 */
public class trst2 {

    public static void main(String[] args) {
        ArrayList<String> urls = new ArrayList<>();
      for(int i=2000;i<=2016;i++){
            for(int k=0;k<=90;k=k+30){
       String  url="http://dblp.uni-trier.de/search/publ/inc?q=ase%20".concat(String.valueOf(i))
       .concat("%20venue%3AASE%3A&h=30&f=").concat(String.valueOf(k)).concat("&s=yvpc");
                urls.add(url);
            }
        }
        for(int k=9;k<=25;k++){
            String url="http://dblp.uni-trier.de/db/journals/tosem/tosem".concat(String.valueOf(k)).concat(".html");
            urls.add(url);
        }
        urls.add("http://dblp.uni-trier.de/db/journals/tosem/tosem21.html");
        urls.add("http://dblp.uni-trier.de/db/journals/tosem/tosem22.html");
        urls.add("http://dblp.uni-trier.de/db/journals/tosem/tosem23.html");
        urls.add("http://dblp.uni-trier.de/db/journals/tosem/tosem24.html");
        urls.add("http://dblp.uni-trier.de/db/journals/tosem/tosem25.html");
        ArrayList<String> list = new ArrayList<>();
        ArrayList pages = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> abstracts = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            Document doc = null;
            try {
                doc = Jsoup.connect(urls.get(i)).timeout(50000).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements links = doc.select("li.drop-down > div.head > a");
            for (Element link : links) {
                String linkHref = link.attr("href");
                if (linkHref.contains("http://ieeexplore") || linkHref.contains("http://doi.acm")
                        ||linkHref.contains("http://dx.doi.org") ||linkHref.contains("http://doi.ieeecomputersociety.org")) {
                    System.out.println(linkHref);
                    list.add(linkHref);
                }
            }
        }
        System.out.println(list.size());
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).contains("http://ieeexplore")||list.get(i).contains("http://dx.doi.org")) {
                try {
                    Document doc = Jsoup.connect(list.get(i)).timeout(50000)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").get();
                    pages.add(i,doc);
                    String s = doc.toString();
                    // System.out.println(s);
                    int c = s.indexOf("\"title\":");
                    int d = s.indexOf(",\"abstract\":");
                    String tit = s.substring(c, d);
                    titles.add(i,tit);
                    int a = s.indexOf("\"abstract\":");
                    int b = s.indexOf(",\"publicationTitle\":");
                    String abs = s.substring(a, b);
                    abstracts.add(i,abs);
                    System.out.println("爬完第"+(i+1)+"个网页");
                } catch (IOException e) {
                    e.printStackTrace();
                    pages.add(i," ");
                    titles.add(i," ");
                    abstracts.add(i," ");
                }
            }
            else if (list.get(i).contains("http://doi.acm")) {
                Document doc = null;
                Document doc2=null;
                try {
                    doc = Jsoup.connect(list.get(i)).timeout(50000)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").get();
                    pages.add(i,doc);
                 String tit =doc.select("div > h1 > strong").first().text();
                    titles.add(i,tit);
                 String s=doc.toString();
                    int a=s.indexOf("tab_abstract.cfm?");
                    int b=s.indexOf("']},ColdFusion.Bind.urlBindHandler,true");
                    String purl=s.substring(a,b);
                    String url="http://dl.acm.org/".concat(purl);
                    doc2=Jsoup.connect(url).timeout(50000)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").get();
                    String abs=doc2.select("body > div > div").text();
                    abstracts.add(i,abs);
                    System.out.println("爬完第"+(i+1)+"个网页");
                } catch (IOException e) {
                    e.printStackTrace();
                    pages.add(i," ");
                    titles.add(i," ");
                    abstracts.add(i," ");
                }

            }
            else if(list.get(i).contains("http://doi.ieeecomputersociety.org")){
                Document doc=null;
                try {
                    doc = Jsoup.connect(list.get(i)).timeout(50000)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").get();
                        pages.add(i,doc);
                   String tit=doc.select("div > div.col-xs-12.col-sm-12.col-md-8.col-lg-8.bgDefault.pb-sm > div:nth-child(1) > div.abstractTitle > a > h2").text();
                    titles.add(i,tit);
                    String abs=doc.select("div > div.col-xs-12.col-sm-12.col-md-8.col-lg-8.bgDefault.pb-sm > div:nth-child(2) > div:nth-child(2)").text();
                      abstracts.add(i,abs);
                    System.out.println("爬完第"+(i+1)+"个网页");
                } catch (IOException e) {
                    e.printStackTrace();
                    pages.add(i," ");
                    titles.add(i," ");
                    abstracts.add(i," ");
                }
            }
        }
       // System.out.println(pages.get(0));
       // System.out.println(titles.get(0));
        //System.out.println(abstracts.get(0));
       for(int i=0;i<titles.size();i++){
           String tit=titles.get(i);
            String abs=abstracts.get(i);
            String url=list.get(i);
            String opinion=null;
            if(abs.contains(" log ")||tit.contains(" log ")
                    ||abs.contains(" logging ")||tit.contains(" logging ")
                    ||abs.contains(" logs ")||tit.contains(" logs ")){
                opinion="accept";
            }
            else{
                opinion="exclude";
            }
            try {
                FileWriter fw=new FileWriter("/Users/Kang/IdeaProjects/crawler/src/tosem3.txt",true);
                fw.write(i+1+"."+"title:"+tit);
                fw.write("\n");
                fw.write(i+1+"."+"abstract:"+abs);
                fw.write("\n");
                fw.write(i+1+"."+"url:"+url);
                fw.write("\n");
                fw.write(i+1+"."+"opinion:"+opinion);
                fw.write("\n");
                fw.write("\n");
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
