package App;

import io.github.humbleui.jwm.*;
import io.github.humbleui.jwm.skija.EventFrameSkija;
import io.github.humbleui.skija.Surface;

import java.io.File;
import java.util.function.Consumer;

public class Application implements Consumer<Event> {

    /** окно приложения
     */
    private final Window window;


    /** конструктор приложения*/
    public Application() {
        window = App.makeWindow();
        window.setEventListener(this);
        window.setTitle("Java 2D");
        window.setWindowSize(1800, 900);
        window.setWindowPosition(100, 100);

        // задаём иконку
        switch (Platform.CURRENT) {
            case WINDOWS -> window.setIcon(new File("src/main/resources/windows.ico"));
            case MACOS -> window.setIcon(new File("src/main/resources/macos.icns"));
        }
        // названия слоёв, которые будем перебирать
        String[] layerNames = new String[]{
                "LayerGLSkija", "LayerRasterSkija"
        };

        // перебираем слои
        for (String layerName : layerNames) {
            String className = "io.github.humbleui.jwm.skija." + layerName;
            try {
                Layer layer = (Layer) Class.forName(className).getDeclaredConstructor().newInstance();
                window.setLayer(layer);
                break;
            } catch (Exception e) {
                System.out.println("Ошибка создания слоя " + className);
            }
        }

        // если окну не присвоен ни один из слоёв
        if (window._layer == null)
            throw new RuntimeException("Нет доступных слоёв для создания");
        window.setVisible(true);
        /**
         *
         * макима гав-гав
         */
    }

    /** Обработчик событий
     * @param e событие
     *
     *          они бывают разные
    */
    @Override
    public void accept(Event e) {
        // если событие - это закрытие окна
        if (e instanceof EventWindowClose) {
            // завершаем работу приложения
            App.terminate();
        } else if (e instanceof EventWindowCloseRequest) {
            window.close();}
                    else if (e instanceof EventFrameSkija ee) {
                // получаем поверхность рисования
                Surface s = ee.getSurface();
                // очищаем её канвас заданным цветом
            s.getCanvas().clear(Colors.APP_BACKGROUND_COLOR);
            }
        }
    }