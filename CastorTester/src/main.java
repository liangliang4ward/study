import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.EventProducer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

public class main {

    public static void main(String args[]) throws IOException, MappingException, MarshalException, ValidationException {

        Mapping      mapping = new Mapping();

            // 1. Load the mapping information from the file
            mapping.loadMapping( "src/mapping.xml" );

            // 2. Unmarshal the data
            Unmarshaller unmar = new Unmarshaller(mapping);
            MyOrder order = (MyOrder)unmar.unmarshal( new InputSource(new FileReader("src/order.xml")));

            // 3. Do some processing on the data
            float total = order.getTotalPrice();
            System.out.println("Order total price = " + total);
            order.setTotal(total);
            // 4. marshal the data with the total price back and print the XML in the console
            StringWriter writer = new StringWriter();
            Marshaller.marshal(order,writer);
            System.out.println(writer);
    }
}