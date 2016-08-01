package Source;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JOptionPane;



public class Code 
{
    private List<String> list=new ArrayList<>();
    Excel_file excel_file=new Excel_file();
    private Logger logger;
    public Code()
    {
        list=new ArrayList<>();
    }
    
    public void scrap_data(String user_input)
    {
        
        try 
        {
            logger = Logger.getLogger("Scrapper Log Files");  
            FileHandler fh=new FileHandler("logfile.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter); 
        } catch (Exception e) 
        {
            System.out.println("Exception in log reading");
        }
        
        
        if(user_input.isEmpty())
        {
            return;
        }
        else
        {
            logger.info("Search started");
            Scrapping_GUI.showtext("Search started..\n");
            
            System.out.println("Working fine....");
            final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);

            String url="https://www.bing.com";

            try 
            {
                HtmlPage page1 = webClient.getPage(url);
                page1.getElementById("sb_form_q").setAttribute("value", user_input);
                page1=page1.getElementById("sb_form_go").click();
                //System.out.println(page1.asText());
                
                List sites = page1.getByXPath("//li[@class=\"b_algo\"]");
                if(sites.size()>0)
                {
                    System.out.println("sites found for user input");
                    logger.info("Sites found for search...\n");
                    Scrapping_GUI.showtext("Sites found for search...\n");
                    
                    
                    for(int i=0;i<sites.size();i++)
                    {
                        logger.info("site:"+(i+1)+"\n");
                        Scrapping_GUI.showtext("site:"+(i+1)+"\n");
                        System.out.println("site: " + (i+1));
                        
                        HtmlListItem item=(HtmlListItem)sites.get(i);
                        DomNodeList<HtmlElement> data = item.getElementsByTagName("a");
                        String link=data.get(0).getAttribute("href");
                        page1=webClient.getPage(link);
                        String source=page1.asXml();
                        get_code(link, source);
                    }
                    excel_file.create_excel(list);
                    System.out.println("\nSearch Completed...");
                    Scrapping_GUI.showtext("Search Completed...");
                }
            }
            catch (Exception e) 
            {
                System.out.println("Something unusuall happen.");
                    logger.info("Error in acessing the site..\n");
                    Scrapping_GUI.showtext("Error in acessing the site..\n");
            }
            System.out.println("\nSearch ended..");
            logger.info("Search Completed Successfully\n");
            Scrapping_GUI.showtext("Search Completed Successfully\n");
            JOptionPane.showMessageDialog(null,"Search Successfull");
        }
    }
    
    
    
    public void get_code(String link,String source)
    {
         try 
            {
                int start=source.indexOf("<!-- Start of Async HubSpot Analytics Code -->");
                int last=source.indexOf("<!-- End of Async HubSpot Analytics Code -->");
                String j_code=source.substring(start, last);
                int begin=j_code.indexOf("<script type=\"text/javascript\">");
                j_code=j_code.substring(begin, j_code.length());
                add_data(link, j_code);
                System.out.println(j_code);
                logger.info("Code Present...\n");
                Scrapping_GUI.showtext("Code Present...\n");
            } 
            catch (Exception e) 
            {
                System.out.println("\nThere is no hubspot Code on this site.");
                 logger.info("No Code Found...\n");
                 Scrapping_GUI.showtext("No Code Found...\n");
            }
    }
    
    
    public void add_data(String site,String code)
    {
        try 
        {
            list.add(site);
            list.add(code);
        }
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(null, "Error in adding data to exel file.");
                logger.info("Error in adding data to excel file\n");
        }
    }
    
    
}
