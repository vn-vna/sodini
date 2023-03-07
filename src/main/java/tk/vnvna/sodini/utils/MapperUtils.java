package tk.vnvna.sodini.utils;

import com.sun.codemodel.JCodeModel;
import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.Jackson2Annotator;
import org.jsonschema2pojo.SchemaGenerator;
import org.jsonschema2pojo.SchemaMapper;
import org.jsonschema2pojo.SchemaStore;
import org.jsonschema2pojo.SourceType;
import org.jsonschema2pojo.rules.RuleFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MapperUtils {
  public static void convertJsonToJavaClass(
    URL inputJsonUrl,
    File outputJavaClassDirectory,
    String packageName,
    String javaClassName)
    throws IOException {
    JCodeModel jcodeModel = new JCodeModel();

    GenerationConfig config = new DefaultGenerationConfig() {
      @Override
      public boolean isGenerateBuilders() {
        return true;
      }

      @Override
      public SourceType getSourceType() {
        return SourceType.JSON;
      }
    };

    SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());
    mapper.generate(jcodeModel, javaClassName, packageName, inputJsonUrl);

    jcodeModel.build(outputJavaClassDirectory);
  }

  public static void main(String[] args) {
    try {
      convertJsonToJavaClass(
        new URL("file:///D:/Data/JSON/anime.json"),
        new File("D:/Data"),
        MapperUtils.class.getPackageName() + ".schemas",
        "KitsuneTrendingSchema");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
