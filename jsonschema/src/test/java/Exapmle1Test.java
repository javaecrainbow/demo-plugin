import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.cfg.ValidationConfigurationBuilder;
import com.github.fge.jsonschema.core.keyword.syntax.checkers.draftv4.DraftV4DependenciesSyntaxChecker;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.library.DraftV4Library;
import com.github.fge.jsonschema.library.Keyword;
import com.github.fge.jsonschema.library.Library;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.messages.JsonSchemaValidationBundle;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.github.fge.msgsimple.source.MapMessageSource;
import com.github.fge.msgsimple.source.MessageSource;
import com.qpaas.eagle.engine.component.sdc.schema.extend.QpaasPathDependenciesDigester;
import com.qpaas.eagle.engine.component.sdc.schema.extend.QpaasPathDependenciesValidator;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * <p/>
 * 测试
 * <p/>
 *
 * @author salkli
 * @date 2021/1/13
 */
public class Exapmle1Test {


    @Test
    @SneakyThrows
    public void injectKeyWord() {
        // 自定义关键词
        Keyword keyword =
            Keyword.newBuilder("path_dependencies").withSyntaxChecker(DraftV4DependenciesSyntaxChecker.getInstance())
                .withDigester(QpaasPathDependenciesDigester.getInstance())
                .withValidatorClass(QpaasPathDependenciesValidator.class).freeze();
        Library library = DraftV4Library.get().thaw().addKeyword(keyword).freeze();
        final String key = "missing node";
        final String value = "{} node is not exist";
        final MessageSource source = MapMessageSource.newBuilder().put(key, value).build();
        final MessageBundle bundle =
            MessageBundles.getBundle(JsonSchemaValidationBundle.class).thaw().appendSource(source).freeze();
        ValidationConfigurationBuilder validationConfigurationBuilder = ValidationConfiguration.newBuilder();
        validationConfigurationBuilder.setValidationMessages(bundle);
        ValidationConfiguration validationConfiguration = validationConfigurationBuilder
            .setDefaultLibrary("http://qpaas-schema.org/draft-04/schema#", library).freeze();
        JsonSchemaFactory factory =
            JsonSchemaFactory.newBuilder().setValidationConfiguration(validationConfiguration).freeze();
        JsonNode schemaNode = JsonLoader.fromResource("/path_dependencies_schema.json");
        JsonNode goodNode = JsonLoader.fromResource("/path_dependencies_schema_good.json");
        JsonSchema jsonSchema = factory.getJsonSchema(schemaNode);
        ProcessingReport validate = jsonSchema.validate(goodNode, true);
        System.out.println(validate);
    }

    @Test
    @SneakyThrows
    public void test1() {
        JsonNode sysInfoSchema = JsonLoader.fromPath("D:\\sdc\\test\\fstab.json");
        JsonNode baseInfo = JsonLoader.fromPath("D:\\sdc\\test\\fstab_good.json");
        System.out.println(sysInfoSchema);
        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        final JsonSchema schema = factory.getJsonSchema(sysInfoSchema);
        ProcessingReport validate = schema.validate(baseInfo, true);
        System.out.println("123");
    }
}
