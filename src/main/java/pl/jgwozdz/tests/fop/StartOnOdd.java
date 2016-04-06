package pl.jgwozdz.tests.fop;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.MimeConstants;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StartOnOdd {
    public static void main(String[] args) throws Exception {
        
//        new FopFactoryBuilder().build();
        FopFactory fopFactory = FopFactory.newInstance(new URI("."));

        Path output = Paths.get("start-on-odd.pdf");
        OutputStream os = Files.newOutputStream(output);

        // Step 3: Construct fop with desired output format
        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, os);

        // Step 4: Setup JAXP using identity transformer
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer(); // identity transformer

        // Step 5: Setup input and output for XSLT transformation
        // Setup input stream
        URL resource = StartOnOdd.class.getResource("/fop/start-on-odd.fo");
        System.out.println("Reading from " + resource);
        Source src = new StreamSource(resource.openStream());

        // Resulting SAX events (the generated FO) must be piped through to FOP
        Result res = new SAXResult(fop.getDefaultHandler());

        // Step 6: Start XSLT transformation and FOP processing
        transformer.transform(src, res);        
        os.close();
        System.out.println("Wrote to " + output.toAbsolutePath());
        
        Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "start", "\"LaunchPDF\"", "\"" + output + "\""});
    }
}
