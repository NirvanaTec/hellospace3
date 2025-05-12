/*
 * 涅槃科技 and 风横
 * https://npyyds.top/
 * https://gitee.com/newNP/
 * https://github.com/NirvanaTec/
 * 最终解释权归涅槃科技所有。
 */
package space.mixin.mapping;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

public interface IMappingFile {
    static IMappingFile load(File path) throws IOException {
        try (InputStream in = new FileInputStream(path)) {
            return load(in);
        }
    }

    static IMappingFile load(InputStream in) throws IOException {
        return InternalUtils.load(in);
    }

    Collection<? extends IPackage> getPackages();

    IPackage getPackage(String original);

    Collection<? extends IClass> getClasses();

    IClass getClass(String original);

    String remapPackage(String pkg);

    String remapClass(String desc);

    String remapDescriptor(String desc);

    void write(Path path, Format format, boolean reversed) throws IOException;

    IMappingFile reverse();

    IMappingFile rename(IRenamer renamer);

    /**
     * Chains this mapping file with another.
     * Any extra mappings in the other file that is not used are discarded.
     * For example:
     * A mapping file with A -> B chained with a mapping file B -> C
     * will result in a chained file of A -> C.
     *
     * @param other the other mapping file to chain with
     * @return the resulting chained mapping file
     */
    IMappingFile chain(IMappingFile other);

    /**
     * Merges this mapping file with another.
     * Any mappings in the other file that already exist in this file will be discarded.
     * All entries in this mapping file are preserved.
     * This operation is purely additive based on the contents of the other file.
     *
     * @param other the other mapping file to merge into this one
     * @return the resulting merged mapping file
     */
    IMappingFile merge(IMappingFile other);

    enum Format {
        SRG(false, false, false),
        XSRG(false, true, false),
        CSRG(false, false, false),
        TSRG(true, false, false),
        TSRG2(true, true, true),
        PG(true, true, false),
        TINY1(false, true, true),
        TINY(true, true, false);

        private final boolean ordered;
        private final boolean hasFieldTypes;
        private final boolean hasNames;

        Format(boolean ordered, boolean hasFieldTypes, boolean hasNames) {
            this.ordered = ordered;
            this.hasFieldTypes = hasFieldTypes;
            this.hasNames = hasNames;
        }

        public static Format get(String name) {
            name = name.toUpperCase(Locale.ENGLISH);
            for (Format value : values())
                if (value.name().equals(name))
                    return value;
            return null;
        }

        public boolean isOrdered() {
            return this.ordered;
        }

        public boolean hasFieldTypes() {
            return this.hasFieldTypes;
        }

        public boolean hasNames() {
            return this.hasNames;
        }
    }

    interface INode {
        String getOriginal();

        String getMapped();

        @Nullable
        String write(Format format, boolean reversed);

        Map<String, String> getMetadata();
    }

    interface IPackage extends INode {
    }

    interface IClass extends INode {
        Collection<? extends IField> getFields();

        Collection<? extends IMethod> getMethods();

        String remapField(String field);

        String remapMethod(String name, String desc);

        @Nullable
        IField getField(String name);

        @Nullable
        IMethod getMethod(String name, String desc);
    }

    interface IOwnedNode<T> extends INode {
        T getParent();
    }

    interface IField extends IOwnedNode<IClass> {
        @Nullable
        String getDescriptor();

        @Nullable
        String getMappedDescriptor();
    }

    interface IMethod extends IOwnedNode<IClass> {
        String getDescriptor();

        String getMappedDescriptor();

        Collection<? extends IParameter> getParameters();

        String remapParameter(int index, String name);

        @Nullable
        IParameter getParameter(int index);
    }

    interface IParameter extends IOwnedNode<IMethod> {
        int getIndex();
    }
}
