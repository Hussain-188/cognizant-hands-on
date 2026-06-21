public class FactoryMethodTest {
    public static void main(String[] args) {
        DocumentFactory factory;
        Document doc;

        factory = new WordDocumentFactory();
        doc = factory.createDocument();
        System.out.println("Created: " + doc.getType());
        doc.open();
        doc.save();

        factory = new PdfDocumentFactory();
        doc = factory.createDocument();
        System.out.println("Created: " + doc.getType());
        doc.open();
        doc.save();

        factory = new ExcelDocumentFactory();
        doc = factory.createDocument();
        System.out.println("Created: " + doc.getType());
        doc.open();
        doc.save();
    }
}
