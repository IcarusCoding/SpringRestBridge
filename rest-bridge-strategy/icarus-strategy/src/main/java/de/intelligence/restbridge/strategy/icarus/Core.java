package de.intelligence.restbridge.strategy.icarus;

public final class Core {

    public static void main(String[] args) throws NoSuchMethodException {
        IcarusRequestTemplateFactory factory = new IcarusRequestTemplateFactory();
        factory.create(Test.class.getDeclaredMethod("test", Object.class, Object.class, Object.class));
    }

}
