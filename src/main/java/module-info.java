module Game {
    requires java.desktop; // Для работы с Java Swing
    requires org.jfree.jfreechart; // Зависимость для JFreeChart
    requires org.apache.logging.log4j; // Зависимость для Log4j API
    requires org.apache.logging.log4j.core;

    exports Game; // Экспорт пакетa Game, чтобы другие модули могли его использовать
}