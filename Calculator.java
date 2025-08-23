import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class Calculator implements WindowListener, 
                                    ActionListener, 
                                    KeyListener {

    GridBagLayout a = new GridBagLayout();
    GridBagConstraints b = new GridBagConstraints();

    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0;
    Button b_c, b_sum, b_dif, b_div, b_mul, b_eq, b_dot, b_back;

    Frame f = new Frame();
    Panel p = new Panel();

    TextField t;

    char operator = ' ';

    public Calculator() {
        p.setLayout(a);
        b.fill = GridBagConstraints.BOTH;
        b.insets = new Insets(1, 1, 1, 1);

        // Display
        b.gridx = 0; b.gridy = 0; b.gridwidth = 4; b.gridheight = 1;
        b.weightx = 0.7;
        b.weighty = 0.7;

        t = new TextField(20);
        t.setEditable(false);
        a.setConstraints(t, b);
        p.add(t);

        // Row 1: C * / Del
        b_c = new Button("C"); b_c.addActionListener(this);
        b.gridx = 0; b.gridy = 1; b.gridwidth = 1; b.gridheight = 1;
        a.setConstraints(b_c, b); p.add(b_c);

        b_div = new Button("/"); b_div.addActionListener(this);
        b.gridx = 1; a.setConstraints(b_div, b); p.add(b_div);

        b_mul = new Button("*"); b_mul.addActionListener(this);
        b.gridx = 2; a.setConstraints(b_mul, b); p.add(b_mul);

        b_back = new Button("Del");
        b_back.addActionListener(this);
        b.gridx = 3; b.gridy = 1; b.gridwidth = 1; b.gridheight = 1;
        a.setConstraints(b_back, b);
        p.add(b_back);

        // Row 2: 1 2 3 +
        b1 = new Button("1"); b1.addActionListener(this);
        b.gridx = 0; b.gridy = 2; a.setConstraints(b1, b); p.add(b1);

        b2 = new Button("2"); b2.addActionListener(this);
        b.gridx = 1; a.setConstraints(b2, b); p.add(b2);

        b3 = new Button("3"); b3.addActionListener(this);
        b.gridx = 2; a.setConstraints(b3, b); p.add(b3);

        b_dif = new Button("-"); b_dif.addActionListener(this);
        b.gridx = 3; b.gridheight = 1; a.setConstraints(b_dif, b); p.add(b_dif);

        // Row 3: 4 5 6
        b4 = new Button("4"); b4.addActionListener(this);
        b.gridx = 0; b.gridy = 3; b.gridheight = 1; a.setConstraints(b4, b); p.add(b4);

        b5 = new Button("5"); b5.addActionListener(this);
        b.gridx = 1; a.setConstraints(b5, b); p.add(b5);

        b6 = new Button("6"); b6.addActionListener(this);
        b.gridx = 2; a.setConstraints(b6, b); p.add(b6);

        b_sum = new Button("+"); b_sum.addActionListener(this);
        b.gridx = 3; b.gridy = 3; b.gridheight = 1; a.setConstraints(b_sum, b); p.add(b_sum);

        // Row 4: 7 8 9 =
        b_eq = new Button("="); b_eq.addActionListener(this);
        b.gridx = 3; b.gridy = 4; b.gridheight = 2; a.setConstraints(b_eq, b); p.add(b_eq);

        b7 = new Button("7"); b7.addActionListener(this);
        b.gridx = 0; b.gridy = 4; b.gridheight = 1; a.setConstraints(b7, b); p.add(b7);

        b8 = new Button("8"); b8.addActionListener(this);
        b.gridx = 1; a.setConstraints(b8, b); p.add(b8);

        b9 = new Button("9"); b9.addActionListener(this);
        b.gridx = 2; a.setConstraints(b9, b); p.add(b9);

        // Row 5: 0 .
        b0 = new Button("0"); b0.addActionListener(this);
        b.gridx = 0; b.gridy = 5; b.gridwidth = 2; a.setConstraints(b0, b); p.add(b0);

        b_dot = new Button("."); b_dot.addActionListener(this);
        b.gridx = 2; b.gridwidth = 1; a.setConstraints(b_dot, b); p.add(b_dot);

        p.setBackground(Color.gray);
        f.add(p);

        f.addWindowListener(this);
        f.pack();
        f.setSize(330, 240);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size   = f.getSize();
        f.setLocation((screen.width - size.width) / 2, (screen.height - size.height) / 2);

        f.setResizable(false);
        f.setVisible(true);

        t.addKeyListener(this);
        f.addKeyListener(this);
        p.addKeyListener(this);
        t.requestFocus();

        EventQueue.invokeLater(() -> t.requestFocusInWindow());
    }

    public static void main(String[] args) {
        new Calculator();
    }

    // ===== Helpers =====
    private String disp() { return t.getText().trim(); }
    private void setDisp(String s) { t.setText(s); }

    private void flash(String message) {
        final String prev = disp();
        setDisp(message);
        new Timer().schedule(new TimerTask() {
            @Override public void run() {
                EventQueue.invokeLater(() -> setDisp(prev));
            }
        }, 2000);
    }

    private int firstOpIndex(String s) {
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (isOperator(c)) return i;
        }
        return -1;
    }

    private boolean endsWithSign(String s) {
        if (s.isEmpty()) return false;
        int n = s.length();
        if (s.charAt(n - 1) != '-') return false;
        if (n == 1) return true;
        return isOperator(s.charAt(n - 2));
    }

    private boolean hasDotInCurrentOperand(String s) {
        int idx = firstOpIndex(s);
        String operand = (idx == -1) ? s : s.substring(idx + 1);
        return operand.contains(".");
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private void showResult(double res) {
        /* String s = String.format(java.util.Locale.US, "%.12f", res);
        s = s.replaceAll("\\.?0+$", "");
        setDisp(s.isEmpty() ? "0" : s); */
        setDisp(formatNumber(res));
    }

    private String formatNumber(double x) {
        if (Double.isNaN(x) || Double.isInfinite(x)) return "Overflow";

        double ax = Math.abs(x);

        if (ax != 0 && (ax < 1e-6 || ax >= 1e12)) {
            java.text.DecimalFormatSymbols sym = java.text.DecimalFormatSymbols.getInstance(java.util.Locale.US);
            java.text.DecimalFormat sci = new java.text.DecimalFormat("0.############E0", sym);
            String s = sci.format(x);          
            s = s.replace("E", "e");
            s = s.replace("e+", "e"); 
            return s;
        }

        String s = String.format(java.util.Locale.US, "%.12f", x);
        s = s.replaceAll("\\.?0+$", "");
        return s;   
    }

    // ===== Events =====
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!(e.getSource() instanceof Button)) return;

        String buttonText = ((Button) e.getSource()).getLabel();
        String cur = disp();

        // Delete
        if (buttonText.equals("Del")) {
            String curText = disp();
            if (!curText.isEmpty()) {
                setDisp(curText.substring(0, curText.length() - 1));
            }
            return;
        }

        // Clear
        if (buttonText.equals("C")) {
            setDisp("");
            operator = ' ';
            return;
        }

        // Digits
        if ("0123456789".contains(buttonText)) {
            setDisp(cur + buttonText);
            return;
        }

        // Dot
        if (buttonText.equals(".")) {
            if (cur.isEmpty() || endsWithSign(cur)) {
                setDisp(cur + "0.");
                return;
            }
            if (hasDotInCurrentOperand(cur)) return; 
            int idx = firstOpIndex(cur);
            String operand = (idx==-1) ? cur : cur.substring(idx+1);
            if (operand.contains(".")) {
                flash("Only one '.' per number!");
                operator=' '; 
                return;
            }
            setDisp(cur + ".");
            return;
        }

        // Minus as sign
        if (buttonText.equals("-")) {
            if (operator == ' ' && cur.isEmpty()) {
                setDisp("-");
                return;
            }
            if (operator != ' ' && !cur.isEmpty() && cur.charAt(cur.length() - 1) == operator) {
                setDisp(cur + "-");
                return;
            }
        }

        // This code is an alternative approach: if the user enters two consecutive operators, it returns an error.
        /* if (isOperator(buttonText.charAt(0))) {
            if (operator!=' ') {
                flash("Do not enter a second operator!");
                operator=' '; 
                return;
            }
            if (cur.isEmpty() && (buttonText.equals("*") || buttonText.equals("/"))) {
                flash("Do not enter '" + buttonText + "' before the first number!");
                operator=' '; 
                return;
            }
            if (cur.isEmpty()) return;

            if (endsWithSign(cur)) return;

            int idx = firstOpIndex(cur);
            if (idx != -1 && idx == cur.length() - 1) {
                setDisp(cur.substring(0, cur.length() - 1) + buttonText);
                operator = buttonText.charAt(0);
                return;
            }

            char last = cur.charAt(cur.length() - 1);
            if (operator == ' ' && (Character.isDigit(last) || last == '.')) {
                setDisp(cur + buttonText);
                operator = buttonText.charAt(0);
            }
            return;
        } */

        // Operators
        if (isOperator(buttonText.charAt(0))) {
            if (cur.isEmpty()) {
                if (buttonText.equals("*") || buttonText.equals("/")) { 
                    flash("You cannot enter '" + buttonText + "' before the first number!"); 
                    return; 
                }
                return;
            }
            if (endsWithSign(cur)) return;

            char last = cur.charAt(cur.length()-1);
            if (isOperator(last)) {
                setDisp(cur.substring(0, cur.length()-1) + buttonText);
                operator = buttonText.charAt(0);
                return;
            }
            setDisp(cur + buttonText);
            operator = buttonText.charAt(0);
            return;
        }

        // Equals
        if (buttonText.equals("=")) {
            if (cur.isEmpty() || endsWithSign(cur)) {
                flash("Complete the expression before '='!");
                operator=' '; 
                return;
            }
            int idx = firstOpIndex(cur);
            if (idx == -1 || idx == cur.length() - 1) return;

            String left  = cur.substring(0, idx);
            String right = cur.substring(idx + 1);
            char op = cur.charAt(idx);

            try {
                double l = Double.parseDouble(left);
                double r = Double.parseDouble(right);
                double res;

                switch (op) {
                    case '+': res = l + r; break;
                    case '-': res = l - r; break;
                    case '*': res = l * r; break;
                    case '/':
                        if (r == 0) { setDisp("Cannot divide by zero"); operator = ' '; return; }
                        res = l / r; break;
                    default: return;
                }

                operator = ' ';
                showResult(res);
            } catch (NumberFormatException ex) {
                flash("Number format error!");
            }
            return;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();

        // Digits
        if (Character.isDigit(c)) {
            setDisp(disp() + c);
        }
        // Operators
        else if (c == '+' || c == '-' || c == '*' || c == '/') {
            actionPerformed(new ActionEvent(new Button(String.valueOf(c)), ActionEvent.ACTION_PERFORMED, ""));
        }
        // Decimal point
        else if (c == '.') {
            actionPerformed(new ActionEvent(new Button("."), ActionEvent.ACTION_PERFORMED, ""));
        }
        // Enter or '='
        else if (c == '\n' || c == '=') {
            actionPerformed(new ActionEvent(new Button("="), ActionEvent.ACTION_PERFORMED, ""));
        }
        // 'c' or 'C' clears
        else if (c == 'c' || c == 'C') {
            actionPerformed(new ActionEvent(new Button("C"), ActionEvent.ACTION_PERFORMED, ""));
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            e.consume();
            String curText = disp();
            if (!curText.isEmpty()) {
                setDisp(curText.substring(0, curText.length() - 1));
            }
        }
    }
    @Override public void keyReleased(KeyEvent e) {}
    
    public void windowOpened(WindowEvent e) {}
    public void windowClosing(WindowEvent e) { System.exit(0); }
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
}
