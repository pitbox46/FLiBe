package github.pitbox46.lithiumforge.common.reflection;

import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReflectionUtil {
    public static boolean hasMethodOverride(Class<?> clazz, Class<?> superclass, boolean fallbackResult, String methodName, Class<?>... methodArgs) {
        while (clazz != null && clazz != superclass && superclass.isAssignableFrom(clazz)) {
            try {
                ObfuscationReflectionHelper.findMethod(clazz, methodName, methodArgs);
                return true;
            } catch (ObfuscationReflectionHelper.UnableToFindMethodException e) {
                if (e.getCause() instanceof NoSuchMethodException) {
                    clazz = clazz.getSuperclass();
                } else if (e.getCause() instanceof NoClassDefFoundError) {
                    Logger logger = LogManager.getLogger("Lithium Class Analysis");
                    logger.warn("Lithium Class Analysis Error: Class " + clazz.getName() + " cannot be analysed, because" +
                            " getting declared methods crashes with NoClassDefFoundError: " + e.getMessage() +
                            ". This is usually caused by modded" +
                            " entities declaring methods that have a return type or parameter type that is annotated" +
                            " with @Environment(value=EnvType.CLIENT). Loading the type is not possible, because" +
                            " it only exists in the CLIENT environment. The recommended fix is to annotate the method with" +
                            " this argument or return type with the same annotation." +
                            " Lithium handles this error by assuming the class cannot be included in some optimizations.");
                    return fallbackResult;
                } else {
                    final String crashedClass = clazz.getName();
                    CrashReport crashReport = CrashReport.forThrowable(e, "Lithium Class Analysis");
                    CrashReportCategory crashReportSection = crashReport.addCategory(e.getClass() + " when getting declared methods.");
                    crashReportSection.setDetail("Analyzed class", crashedClass);
                    crashReportSection.setDetail("Analyzed method name", methodName);
                    crashReportSection.setDetail("Analyzed method args", methodArgs);

                    throw new ReportedException(crashReport);
                }
            } catch (Throwable e) {
                final String crashedClass = clazz.getName();
                CrashReport crashReport = CrashReport.forThrowable(e, "Lithium Class Analysis");
                CrashReportCategory crashReportSection = crashReport.addCategory(e.getClass() + " when getting declared methods.");
                crashReportSection.setDetail("Analyzed class", crashedClass);
                crashReportSection.setDetail("Analyzed method name", methodName);
                crashReportSection.setDetail("Analyzed method args", methodArgs);

                throw new ReportedException(crashReport);
            }
        }
        return false;
    }
}
