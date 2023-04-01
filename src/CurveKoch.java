import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import javax.swing.JApplet;
import javax.swing.JFrame;

public class CurveKoch extends JApplet {

    private boolean drawn = false;
    private Graphics2D g2 = null;

    @Override
    public void paint(Graphics g) {

        if (drawn)
            return;
        drawn = true;

        super.paint(g);

        g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.gray);

        /* Расчет координат треугольника для прорисовки снежинки Коха */
        double a = 300; // Длина стороны треугольника (px)
        double p1x = 250; // Координата x нижней левой точки основания треугольника (px)
        double p1y = 400; // Координата y нижней левой точки основания треугольника (px)
        double p2x = p1x + a;
        double p2y = p1y;
        double h = Math.sqrt(Math.pow(a, 2) - Math.pow((a / 2), 2) / 4);
        double pmx = (p1x + p2x) / 2;
        double pmy = (p1y + p2y) / 2;
        double p3x = pmx + (h * (p1y - pmy)) / (a / 2);
        double p3y = pmy + (h * (p1x - pmx)) / (a / 2);

        /* Сравнительный тест быстроты рекурсионного алгоритма (drawCurveKochRecur)
         * и алгоритма с циклом и массивом (drawCurveKoch).
         * Рисуются последовательно 4 одинаковые кривые Коха разными алгоритмами.
         * Рекурсивный алгоритм выполняется намного медленнее.
         * Возможно, это вызвано особенностями реализации конкретно
         * этого примера на Java. Прежде чем делать выводы, необходим
         * подробный анализ алгоритма.
         */

        drawCurveKochRecur(new Line2D.Double(600,600,200,600), 10);




    }



    /** Нарисовать кривую Коха на основе линии line.
     * Колическтво итераций - maxIter. Текущая итерация рекурсии - curIter.
     *
     * Рекурсивный алгоритм.
     * В данном алгоритме решена проблема "стирания" среднего отрезка линии.
     * Т.е., в итоге действительно получается нарисованна только кривая,
     * без вспомогательных линий.
     * Работает немного медленней, чем с циклом и массивом,
     */
    private void drawCurveKochRecur(Line2D line, int maxIter, int curIter) {

        if (curIter == maxIter) // убрав эту проверку, будут прорисованы все линии, а не только сама кривая
            drawLine(line);

        if (curIter<=maxIter){

            double a = line.getP1().distance(line.getP2());
            a = a / 3;
            //g2.drawString(String.valueOf(a), 10, Integer.parseInt(String.valueOf(Math.round(10+(i*10)))));
            double h = Math.sqrt(Math.pow(a, 2) - Math.pow((a / 2), 2) / 4);

            Point2D ps = line.getP1();
            Point2D pe = line.getP2();

            Point2D pm = new Point2D.Double((ps.getX() + pe.getX()) / 2, (ps.getY() + pe.getY()) / 2);
            Point2D p1 = new Point2D.Double((2 * ps.getX() + pe.getX()) / 3, (2 * ps.getY() + pe.getY()) / 3);
            Point2D p2 = new Point2D.Double((2 * pe.getX() + ps.getX()) / 3, (2 * pe.getY() + ps.getY()) / 3);
            Point2D p3 = new Point2D.Double(
                    pm.getX() + (h * (-p2.getY() + pm.getY())) / (a / 2),
                    pm.getY() + (h * (p2.getX() - pm.getX())) / (a / 2)
            );

            // Рекурсия
            curIter++;
            drawCurveKochRecur(new Line2D.Double(ps,p1), maxIter, curIter);
            drawCurveKochRecur(new Line2D.Double(p1,p3), maxIter, curIter);
            drawCurveKochRecur(new Line2D.Double(p3,p2), maxIter, curIter);
            drawCurveKochRecur(new Line2D.Double(p2,pe), maxIter, curIter);

        }
    }

    private void drawCurveKochRecur(Line2D line, int maxIter) {
        drawCurveKochRecur(line, maxIter, 0);
    }

    /** Нарисовать линию */
    public void drawLine(Line2D line) {
        g2.draw(new Line2D.Double(line.getP1(), line.getP2()));
    }

    /** Run main */
    public static void main(String s[]) {
        JFrame f = new JFrame("ShapesDemo2D");
        f.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        JApplet applet = new CurveKoch();
        f.getContentPane().add("Center", applet);
        applet.init();
        f.pack();
        f.setSize(new Dimension(800, 800));
        f.show();
    }
}