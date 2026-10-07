import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

public class AuraGame extends JFrame {

    // ---------------------------------------------------------
    // 1. AURA DEFINITIONS (42 AURAS SORTED COMMON -> RAREST)
    // ---------------------------------------------------------
    public static class Aura {
        String name;
        double chance;
        long value;
        Color color;
        String shapeStyle;
        int particleCount;
        String pose;

        public Aura(String name, double chance, long value, Color color, String shapeStyle, int particleCount, String pose) {
            this.name = name;
            this.chance = chance;
            this.value = value;
            this.color = color;
            this.shapeStyle = shapeStyle;
            this.particleCount = particleCount;
            this.pose = pose;
        }
    }

    private static final Map<String, Aura> AURAS = new LinkedHashMap<>();

    static {
        // Original & New Auras Ordered from Common to Rarest
        AURAS.put("Common",           new Aura("Common",           1.0 / 2,            5,           new Color(148, 163, 184), "CIRCLE", 4, "DEFAULT"));
        AURAS.put("Uncommon",         new Aura("Uncommon",         1.0 / 5,            15,          new Color(34, 197, 94),   "RING", 6, "DEFAULT"));
        AURAS.put("Breeze",           new Aura("Breeze",           1.0 / 10,           30,          new Color(167, 243, 208), "RING", 6, "FLOAT"));
        AURAS.put("Rare",             new Aura("Rare",             1.0 / 20,          50,          new Color(56, 189, 248),  "ORBIT", 8, "HERO"));
        AURAS.put("Ember",            new Aura("Ember",            1.0 / 40,          120,         new Color(251, 146, 60),  "SPIKES", 8, "POWER"));
        AURAS.put("Gilded",           new Aura("Gilded",           1.0 / 75,           250,         new Color(234, 179, 8),   "RING", 10, "FLEX"));
        AURAS.put("Toxic",            new Aura("Toxic",            1.0 / 150,          600,         new Color(132, 204, 22),  "STARBURST", 10, "DEFAULT"));
        AURAS.put("Epic",             new Aura("Epic",             1.0 / 300,          1200,        new Color(168, 85, 247),  "SPIKES", 12, "POWER"));
        AURAS.put("Magnetic",         new Aura("Magnetic",         1.0 / 500,          2200,        new Color(217, 70, 239),  "ORBIT", 12, "FLEX"));
        AURAS.put("Glacier",          new Aura("Glacier",          1.0 / 800,          4000,        new Color(186, 230, 253), "STARBURST", 14, "FLOAT"));
        AURAS.put("Volt",             new Aura("Volt",             1.0 / 1500,         8000,        new Color(250, 204, 21),  "SPIKES", 16, "POWER"));
        AURAS.put("Solar",            new Aura("Solar",            1.0 / 2200,         12000,       new Color(253, 224, 71),  "STARBURST", 15, "HERO"));
        AURAS.put("Radiant",          new Aura("Radiant",          1.0 / 3000,         18000,       new Color(236, 72, 153),  "STARBURST", 16, "HERO"));
        AURAS.put("Mirage",           new Aura("Mirage",           1.0 / 5000,         32000,       new Color(244, 114, 182), "PRISM", 16, "FLOAT"));
        AURAS.put("Supernova",        new Aura("Supernova",        1.0 / 9000,         70000,       new Color(249, 115, 22),  "SUPERNOVA", 18, "FLEX"));
        AURAS.put("Inferno",          new Aura("Inferno",          1.0 / 18000,        160000,      new Color(225, 29, 72),   "SUPERNOVA", 18, "POWER"));
        AURAS.put("Legendary",        new Aura("Legendary",        1.0 / 30000,        300000,      new Color(234, 179, 8),   "SUPERNOVA", 20, "FLEX"));
        AURAS.put("Vortex",           new Aura("Vortex",           1.0 / 60000,        700000,      new Color(99, 102, 241),  "BLACKHOLE", 22, "POWER"));
        AURAS.put("Celestial",        new Aura("Celestial",        1.0 / 120000,      1500000,     new Color(6, 182, 212),   "PRISM", 24, "FLOAT"));
        AURAS.put("Specter",          new Aura("Specter",          1.0 / 300000,      3800000,     new Color(203, 213, 225), "PRISM", 24, "FLOAT"));
        AURAS.put("Voidborn",         new Aura("Voidborn",         1.0 / 600000,       8000000,     new Color(147, 51, 234),  "PRISM", 26, "POWER"));
        AURAS.put("Superconductor",   new Aura("Superconductor",   1.0 / 1200000,      18000000,    new Color(56, 189, 248),  "SPIKES", 28, "HERO"));
        AURAS.put("Singularity",      new Aura("Singularity",      1.0 / 3000000,      45000000,    new Color(99, 102, 241),  "BLACKHOLE", 30, "FLOAT"));
        AURAS.put("Eclipse",          new Aura("Eclipse",          1.0 / 7500000,      120000000,   new Color(244, 63, 94),   "BLACKHOLE", 32, "POWER"));
        AURAS.put("Hyperdrive",       new Aura("Hyperdrive",       1.0 / 15000000,     250000000,   new Color(20, 184, 166),  "SPIKES", 34, "HERO"));
        AURAS.put("Supergiant",       new Aura("Supergiant",       1.0 / 35000000,     600000000,   new Color(251, 146, 60),  "GALAXY", 36, "FLEX"));
        AURAS.put("Chronos",          new Aura("Chronos",          1.0 / 90000000L,    1800000000L, new Color(251, 191, 36),  "ORBIT", 38, "FLOAT"));
        AURAS.put("Overlord",         new Aura("Overlord",         1.0 / 250000000L,   4500000000L, new Color(159, 18, 57),   "GOD", 40, "POWER"));
        AURAS.put("Gargantua",        new Aura("Gargantua",        1.0 / 600000000L,   12000000000L,new Color(239, 68, 68),   "GALAXY", 42, "POWER"));
        AURAS.put("NEBULA",           new Aura("NEBULA",           1.0 / 2500000000L,  50000000000L,new Color(192, 132, 252), "GALAXY", 46, "FLOAT"));
        AURAS.put("Quantum",          new Aura("Quantum",          1.0 / 10000000000L, 220000000000L,new Color(52, 211, 153), "PRISM", 48, "HERO"));
        AURAS.put("ABSOLUTE ZERO",    new Aura("ABSOLUTE ZERO",    1.0 / 60000000000L, 1500000000000L,new Color(207, 250, 254), "GOD", 50, "HERO"));
        AURAS.put("SUPERPOSITION",    new Aura("SUPERPOSITION",    1.0 / 200000000000L,4500000000000L,new Color(165, 243, 252),"GOD", 55, "FLOAT"));
        AURAS.put("INFINITY",         new Aura("INFINITY",         1.0 / 600000000000L,18000000000000L,new Color(244, 114, 182),"GOD", 60, "FLEX"));
        AURAS.put("DARK MATTER",      new Aura("DARK MATTER",      1.0 / 1500000000000L,40000000000000L,new Color(76, 29, 149), "BLACKHOLE", 65, "POWER"));
        AURAS.put("THE OMNIPRESENT",  new Aura("THE OMNIPRESENT",  1.0 / 12000000000000L, 600000000000000L, new Color(255, 255, 255), "GOD", 80, "FLOAT"));
        AURAS.put("BIG BANG",         new Aura("BIG BANG",         1.0 / 40000000000000L, 2000000000000000L, new Color(250, 204, 21), "SUPERNOVA", 90, "POWER"));
        AURAS.put("CREATOR",          new Aura("CREATOR",          1.0 / 100000000000000L, 9000000000000000L, new Color(251, 146, 60), "GOD", 100, "POWER"));
        AURAS.put("MULTIVERSE",       new Aura("MULTIVERSE",       1.0 / 500000000000000L, 30000000000000000L, new Color(192, 38, 211), "GALAXY", 110, "FLOAT"));
        AURAS.put("ARCHITECT",        new Aura("ARCHITECT",        1.0 / 2000000000000000L, 100000000000000000L, new Color(38, 38, 38), "GOD", 120, "HERO"));
        AURAS.put("EXISTENCE",        new Aura("EXISTENCE",        1.0 / 10000000000000000L, 800000000000000000L, new Color(254, 240, 138), "GOD", 130, "FLEX"));
        AURAS.put("ZENITH",           new Aura("ZENITH",           1.0 / 100000000000000000L, 999999999999999999L, new Color(255, 255, 255), "GOD", 150, "FLOAT"));
    }

    // Weather Events
    public static class WeatherEvent {
        String name;
        double multiplier;
        Color color;

        public WeatherEvent(String name, double multiplier, Color color) {
            this.name = name;
            this.multiplier = multiplier;
            this.color = color;
        }
    }

    private static final List<WeatherEvent> WEATHER_EVENTS = Arrays.asList(
            new WeatherEvent("CLEAR VOID", 1.0, new Color(148, 163, 184)),
            new WeatherEvent("SOLAR FLARE", 2.5, new Color(234, 179, 8)),
            new WeatherEvent("VOID STORM", 5.0, new Color(168, 85, 247)),
            new WeatherEvent("COSMIC ALIGNMENT", 10.0, new Color(6, 182, 212))
    );

    // State Variables
    private long totalRolls = 0;
    private long shards = 0;
    private long rebirthTokens = 0;

    private double baseLuck = 1.0;
    private long lensUpgradeCost = 50;

    private int autoDelayMs = 300;
    private long speedUpgradeCost = 250;

    private int shardMultiplierLevel = 1;
    private long shardUpgradeCost = 500;

    private boolean isRolling = false;
    private boolean autoActive = false;
    private String currentAuraName = "Common";
    private String equippedAuraName = "Common";

    private WeatherEvent currentWeather = WEATHER_EVENTS.get(0);
    private int weatherTimer = 10;

    private final Map<String, Long> inventory = new LinkedHashMap<>();

    // Visual FX
    private float popScale = 1.0f;
    private float flashOpacity = 0.0f;
    private int shakeIntensity = 0;

    // UI Elements
    private JLabel lblRolls, lblShards, lblRebirths, lblLuck, lblWeather, lblStatus, lblAuraName, lblOdds, lblEquipped, lblIndexHeader;
    private JButton btnRoll, btnAuto, btnAdmin, btnUpgradeLens, btnUpgradeSpeed, btnUpgradeShard, btnRebirth;
    private JPanel invPanel;
    private FigurePanel figurePanel;
    private CorePanel corePanel;

    // Admin Console UI Components
    private JTextArea adminConsoleLog;

    public AuraGame() {
        for (String key : AURAS.keySet()) {
            inventory.put(key, 0L);
        }
        inventory.put("Common", 1L);

        setTitle("AURA RNG // DUAL-STAGE INDEX EDITION");
        setSize(700, 980);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(11, 15, 25));
        setLayout(new BorderLayout(10, 10));

        setupUI();
        startTimers();
    }

    // ---------------------------------------------------------
    // 2. UI LAYOUT
    // ---------------------------------------------------------
    private void setupUI() {
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBackground(new Color(11, 15, 25));
        topContainer.setBorder(BorderFactory.createEmptyBorder(10, 12, 0, 12));

        // HUD Dashboard
        JPanel hud = new JPanel(new GridLayout(1, 4, 8, 0));
        hud.setBackground(new Color(17, 24, 39));
        hud.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(31, 41, 55), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        lblRolls = createLabel("ROLLS: 0", new Color(56, 189, 248));
        lblShards = createLabel("SHARDS: 0", new Color(250, 204, 21));
        lblLuck = createLabel("LUCK: 1.00x", new Color(74, 222, 128));
        lblRebirths = createLabel("REBIRTHS: 0", new Color(244, 114, 182));

        hud.add(lblRolls);
        hud.add(lblShards);
        hud.add(lblLuck);
        hud.add(lblRebirths);
        topContainer.add(hud);

        topContainer.add(Box.createRigidArea(new Dimension(0, 6)));

        // Weather Banner
        lblWeather = new JLabel("EVENT: CLEAR VOID (1.0x) | Resets in 10s", SwingConstants.CENTER);
        lblWeather.setFont(new Font("Monospaced", Font.BOLD, 11));
        lblWeather.setForeground(currentWeather.color);
        lblWeather.setOpaque(true);
        lblWeather.setBackground(new Color(17, 24, 39));
        lblWeather.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(31, 41, 55), 1),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));
        lblWeather.setAlignmentX(Component.CENTER_ALIGNMENT);
        topContainer.add(lblWeather);

        add(topContainer, BorderLayout.NORTH);

        // Center Dual Stage (Stick Figure Left | Core Generator Right)
        JPanel stageContainer = new JPanel(new GridLayout(1, 2, 10, 0));
        stageContainer.setBackground(new Color(11, 15, 25));
        stageContainer.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

        figurePanel = new FigurePanel();
        corePanel = new CorePanel();

        stageContainer.add(figurePanel);
        stageContainer.add(corePanel);

        // Main Center Area Wrapper
        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setBackground(new Color(11, 15, 25));
        centerWrapper.add(stageContainer);
        centerWrapper.add(Box.createRigidArea(new Dimension(0, 6)));

        lblEquipped = new JLabel("EQUIPPED: COMMON", SwingConstants.CENTER);
        lblEquipped.setFont(new Font("Monospaced", Font.BOLD, 11));
        lblEquipped.setForeground(new Color(148, 163, 184));
        lblEquipped.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerWrapper.add(lblEquipped);

        lblStatus = new JLabel("QUANTUM CORE READY", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Monospaced", Font.BOLD, 11));
        lblStatus.setForeground(new Color(100, 116, 139));
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerWrapper.add(lblStatus);

        lblAuraName = new JLabel("READY", SwingConstants.CENTER);
        lblAuraName.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblAuraName.setForeground(new Color(56, 189, 248));
        lblAuraName.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerWrapper.add(lblAuraName);

        lblOdds = new JLabel("Press Roll to synthesize...", SwingConstants.CENTER);
        lblOdds.setFont(new Font("Monospaced", Font.PLAIN, 11));
        lblOdds.setForeground(new Color(148, 163, 184));
        lblOdds.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerWrapper.add(lblOdds);

        centerWrapper.add(Box.createRigidArea(new Dimension(0, 8)));

        // Actions (Roll, Auto, and Admin Panel Buttons)
        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 8, 0));
        btnPanel.setBackground(new Color(11, 15, 25));
        btnPanel.setMaximumSize(new Dimension(660, 34));
        btnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnRoll = new JButton("⚡ ROLL AURA");
        styleButton(btnRoll, new Color(56, 189, 248), Color.BLACK);
        btnRoll.addActionListener(e -> startRollSequence());

        btnAuto = new JButton("⚙ AUTO: OFF");
        styleButton(btnAuto, new Color(239, 68, 68), Color.WHITE);
        btnAuto.addActionListener(e -> toggleAuto());

        btnAdmin = new JButton("🛠 ADMIN PANEL");
        styleButton(btnAdmin, new Color(168, 85, 247), Color.WHITE);
        btnAdmin.addActionListener(e -> openAdminPanel());

        btnPanel.add(btnRoll);
        btnPanel.add(btnAuto);
        btnPanel.add(btnAdmin);
        centerWrapper.add(btnPanel);

        centerWrapper.add(Box.createRigidArea(new Dimension(0, 8)));

        // On-Screen Admin Command Output Console
        adminConsoleLog = new JTextArea(3, 50);
        adminConsoleLog.setEditable(false);
        adminConsoleLog.setFont(new Font("Monospaced", Font.PLAIN, 10));
        adminConsoleLog.setBackground(new Color(5, 8, 15));
        adminConsoleLog.setForeground(new Color(34, 197, 94));
        adminConsoleLog.setText("> System initialized. Admin ready.\n");

        JScrollPane logScroll = new JScrollPane(adminConsoleLog);
        logScroll.setMaximumSize(new Dimension(660, 50));
        logScroll.setPreferredSize(new Dimension(660, 50));
        logScroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        logScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(31, 41, 55), 1),
                "LIVE ADMIN COMMAND OUTPUT",
                0, 0,
                new Font("Monospaced", Font.BOLD, 9),
                new Color(168, 85, 247)
        ));
        centerWrapper.add(logScroll);

        add(centerWrapper, BorderLayout.CENTER);

        // Bottom Controls (Upgrades & Index Inventory)
        JPanel bottomContainer = new JPanel();
        bottomContainer.setLayout(new BoxLayout(bottomContainer, BoxLayout.Y_AXIS));
        bottomContainer.setBackground(new Color(11, 15, 25));
        bottomContainer.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        // Balanced 2x2 Grid for Upgrades and Rebirth
        JPanel upgradesGrid = new JPanel(new GridLayout(2, 2, 8, 8));
        upgradesGrid.setBackground(new Color(11, 15, 25));
        upgradesGrid.setMaximumSize(new Dimension(660, 60));
        upgradesGrid.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnUpgradeLens = new JButton("Lens (+0.5x) | 50 S");
        styleButton(btnUpgradeLens, new Color(31, 41, 55), Color.WHITE);
        btnUpgradeLens.addActionListener(e -> buyLensUpgrade());

        btnUpgradeSpeed = new JButton("Auto Speed | 250 S");
        styleButton(btnUpgradeSpeed, new Color(31, 41, 55), Color.WHITE);
        btnUpgradeSpeed.addActionListener(e -> buySpeedUpgrade());

        btnUpgradeShard = new JButton("Shard Gain (+1x) | 500 S");
        styleButton(btnUpgradeShard, new Color(31, 41, 55), Color.WHITE);
        btnUpgradeShard.addActionListener(e -> buyShardUpgrade());

        btnRebirth = new JButton("🌀 REBIRTH (1,000,000 S)");
        styleButton(btnRebirth, new Color(244, 114, 182), Color.BLACK);
        btnRebirth.addActionListener(e -> performRebirth());

        upgradesGrid.add(btnUpgradeLens);
        upgradesGrid.add(btnUpgradeSpeed);
        upgradesGrid.add(btnUpgradeShard);
        upgradesGrid.add(btnRebirth);

        bottomContainer.add(upgradesGrid);
        bottomContainer.add(Box.createRigidArea(new Dimension(0, 8)));

        lblIndexHeader = new JLabel("📖 AURA INDEX / CODEX (DISCOVERED: 1 / " + AURAS.size() + ")", SwingConstants.CENTER);
        lblIndexHeader.setFont(new Font("Monospaced", Font.BOLD, 10));
        lblIndexHeader.setForeground(new Color(148, 163, 184));
        lblIndexHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomContainer.add(lblIndexHeader);
        bottomContainer.add(Box.createRigidArea(new Dimension(0, 4)));

        invPanel = new JPanel();
        invPanel.setLayout(new BoxLayout(invPanel, BoxLayout.Y_AXIS));
        invPanel.setBackground(new Color(17, 24, 39));

        JScrollPane scrollPane = new JScrollPane(invPanel);
        scrollPane.setPreferredSize(new Dimension(660, 150));
        scrollPane.setMaximumSize(new Dimension(660, 150));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(31, 41, 55), 1));
        bottomContainer.add(scrollPane);

        add(bottomContainer, BorderLayout.SOUTH);

        updateInventoryUI();
        updateRebirthButtonText();
    }

    private JLabel createLabel(String text, Color fg) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Monospaced", Font.BOLD, 10));
        lbl.setForeground(fg);
        return lbl;
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setFont(new Font("Monospaced", Font.BOLD, 10));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(55, 65, 81), 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // ---------------------------------------------------------
    // ADMIN PANEL & COMMAND EXECUTOR
    // ---------------------------------------------------------
    private void logAdminCommand(String msg) {
        adminConsoleLog.append("> " + msg + "\n");
        adminConsoleLog.setCaretPosition(adminConsoleLog.getDocument().getLength());
    }

    private void openAdminPanel() {
        JDialog adminDialog = new JDialog(this, "ADMIN COMMAND CONSOLE", false);
        adminDialog.setSize(480, 360);
        adminDialog.setLocationRelativeTo(this);
        adminDialog.setLayout(new BorderLayout(8, 8));
        adminDialog.getContentPane().setBackground(new Color(17, 24, 39));

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBackground(new Color(17, 24, 39));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JTextField cmdField = new JTextField();
        cmdField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        cmdField.setBackground(new Color(11, 15, 25));
        cmdField.setForeground(Color.CYAN);

        JButton execBtn = new JButton("EXECUTE");
        styleButton(execBtn, new Color(168, 85, 247), Color.WHITE);

        topPanel.add(new JLabel("COMMAND: ") {{ setForeground(Color.WHITE); setFont(new Font("Monospaced", Font.BOLD, 11)); }}, BorderLayout.WEST);
        topPanel.add(cmdField, BorderLayout.CENTER);
        topPanel.add(execBtn, BorderLayout.EAST);

        adminDialog.add(topPanel, BorderLayout.NORTH);

        // Quick Macros Panel
        JPanel macroPanel = new JPanel(new GridLayout(3, 2, 6, 6));
        macroPanel.setBackground(new Color(17, 24, 39));
        macroPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(31, 41, 55), 1),
                "QUICK ADMIN MACROS",
                0, 0,
                new Font("Monospaced", Font.BOLD, 10),
                new Color(168, 85, 247)
        ));

        JButton m1 = new JButton("+1M Shards");
        JButton m2 = new JButton("+100 Luck");
        JButton m3 = new JButton("+1000 Rolls");
        JButton m4 = new JButton("Give ZENITH");
        JButton m5 = new JButton("Give INFINITY");
        JButton m6 = new JButton("Clear Console");

        styleButton(m1, new Color(31, 41, 55), Color.WHITE);
        styleButton(m2, new Color(31, 41, 55), Color.WHITE);
        styleButton(m3, new Color(31, 41, 55), Color.WHITE);
        styleButton(m4, new Color(31, 41, 55), Color.WHITE);
        styleButton(m5, new Color(31, 41, 55), Color.WHITE);
        styleButton(m6, new Color(31, 41, 55), Color.WHITE);

        m1.addActionListener(e -> executeCommand("give shards 1000000"));
        m2.addActionListener(e -> executeCommand("give luck 100"));
        m3.addActionListener(e -> executeCommand("give rolls 1000"));
        m4.addActionListener(e -> executeCommand("give aura ZENITH"));
        m5.addActionListener(e -> executeCommand("give aura INFINITY"));
        m6.addActionListener(e -> executeCommand("clear log"));

        macroPanel.add(m1); macroPanel.add(m2);
        macroPanel.add(m3); macroPanel.add(m4);
        macroPanel.add(m5); macroPanel.add(m6);

        adminDialog.add(macroPanel, BorderLayout.CENTER);

        // Command Cheat Sheet
        JTextArea helpArea = new JTextArea(
                "COMMAND SYNTAX:\n" +
                        " • give shards <amount>\n" +
                        " • give luck <amount>\n" +
                        " • give rolls <amount>\n" +
                        " • give aura <aura_name>\n" +
                        " • clear log"
        );
        helpArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        helpArea.setBackground(new Color(11, 15, 25));
        helpArea.setForeground(new Color(148, 163, 184));
        helpArea.setEditable(false);
        helpArea.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        adminDialog.add(helpArea, BorderLayout.SOUTH);

        ActionListener runCmd = e -> {
            String text = cmdField.getText().trim();
            if (!text.isEmpty()) {
                executeCommand(text);
                cmdField.setText("");
            }
        };

        cmdField.addActionListener(runCmd);
        execBtn.addActionListener(runCmd);

        adminDialog.setVisible(true);
    }

    private void executeCommand(String rawCmd) {
        String cmd = rawCmd.trim();
        String lower = cmd.toLowerCase();

        if (lower.equals("clear log")) {
            adminConsoleLog.setText("");
            logAdminCommand("Console cleared.");
            return;
        }

        if (lower.startsWith("give shards ")) {
            try {
                long val = Long.parseLong(cmd.substring(12).trim());
                shards += val;
                lblShards.setText("SHARDS: " + String.format("%,d", shards));
                logAdminCommand(String.format("COMMAND EXEC: Added %,d Shards.", val));
            } catch (Exception ex) {
                logAdminCommand("ERROR: Invalid shard number format.");
            }
        } else if (lower.startsWith("give luck ")) {
            try {
                double val = Double.parseDouble(cmd.substring(10).trim());
                baseLuck += val;
                updateLuckLabel();
                logAdminCommand(String.format("COMMAND EXEC: Increased Base Luck by +%.2fx.", val));
            } catch (Exception ex) {
                logAdminCommand("ERROR: Invalid luck number format.");
            }
        } else if (lower.startsWith("give rolls ")) {
            try {
                long val = Long.parseLong(cmd.substring(11).trim());
                totalRolls += val;
                lblRolls.setText("ROLLS: " + String.format("%,d", totalRolls));
                logAdminCommand(String.format("COMMAND EXEC: Added %,d Rolls.", val));
            } catch (Exception ex) {
                logAdminCommand("ERROR: Invalid rolls number format.");
            }
        } else if (lower.startsWith("give aura ")) {
            String auraTarget = cmd.substring(10).trim();
            Aura matched = null;
            for (String key : AURAS.keySet()) {
                if (key.equalsIgnoreCase(auraTarget)) {
                    matched = AURAS.get(key);
                    break;
                }
            }

            if (matched != null) {
                inventory.put(matched.name, inventory.getOrDefault(matched.name, 0L) + 1);
                updateInventoryUI();
                logAdminCommand(String.format("COMMAND EXEC: Granted Aura [%s] to Inventory!", matched.name));
            } else {
                logAdminCommand("ERROR: Unknown Aura name. Check spelling.");
            }
        } else {
            logAdminCommand("ERROR: Unknown Command -> " + cmd);
        }
    }

    // ---------------------------------------------------------
    // 3. STICK FIGURE SHOWROOM PANEL (PERSISTENT EQUIPPED AURA)
    // ---------------------------------------------------------
    private class FigurePanel extends JPanel {
        private double rotationAngle = 0;
        private double floatOffset = 0;
        private final Random rand = new Random();

        public FigurePanel() {
            setBackground(new Color(17, 24, 39));
            setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(31, 41, 55), 1),
                    "AVATAR SHOWROOM",
                    0, 0,
                    new Font("Monospaced", Font.BOLD, 10),
                    new Color(148, 163, 184)
            ));
        }

        public void updateAnimation() {
            rotationAngle += 0.04;
            floatOffset = Math.sin(rotationAngle * 2) * 4;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Aura equipped = AURAS.getOrDefault(equippedAuraName, AURAS.get("Common"));
            int cx = getWidth() / 2;
            int cy = getHeight() / 2 + (equipped.pose.equals("FLOAT") ? (int) floatOffset : 0);

            g2d.translate(cx, cy);

            // Background Glow
            float[] dist = {0.0f, 1.0f};
            Color[] colors = {equipped.color, new Color(0, 0, 0, 0)};
            RadialGradientPaint p = new RadialGradientPaint(0, 0, 75, dist, colors);
            g2d.setPaint(p);
            g2d.fill(new Ellipse2D.Double(-75, -75, 150, 150));

            // CUSTOM ANIMATED VISUAL FX FOR ALL AURAS
            renderCustomAuraEffect(g2d, equipped.name, equipped.color);

            // Stick figure rendering
            drawStickFigure(g2d, equipped.pose);

            g2d.dispose();
        }

        private void renderCustomAuraEffect(Graphics2D g, String name, Color color) {
            g.setColor(color);

            switch (name) {
                case "Common": {
                    g.setStroke(new BasicStroke(1.0f));
                    for (int i = 0; i < 6; i++) {
                        double dy = -((rotationAngle * 20 + i * 15) % 60);
                        double dx = Math.sin(rotationAngle + i) * 12;
                        g.drawOval((int) dx - 2, (int) dy + 20, 4, 4);
                    }
                    break;
                }
                case "Uncommon": {
                    g.setStroke(new BasicStroke(1.5f));
                    for (int i = 0; i < 5; i++) {
                        double y = 30 - ((rotationAngle * 30 + i * 20) % 70);
                        double x = (i % 2 == 0 ? 1 : -1) * (15 + Math.sin(rotationAngle * 2 + i) * 5);
                        g.draw(new Arc2D.Double(x - 5, y - 5, 10, 10, 0, 180, Arc2D.OPEN));
                    }
                    break;
                }
                case "Breeze": {
                    g.setStroke(new BasicStroke(1.5f));
                    for (int i = 0; i < 4; i++) {
                        double a = rotationAngle * 2 + i * Math.PI / 2;
                        g.draw(new Arc2D.Double(Math.cos(a) * 30 - 15, Math.sin(a) * 15 - 15, 30, 30, 0, 90, Arc2D.OPEN));
                    }
                    break;
                }
                case "Rare": {
                    g.setStroke(new BasicStroke(2.0f));
                    for (int i = 0; i < 3; i++) {
                        double r = ((rotationAngle * 25 + i * 20) % 60);
                        g.draw(new Ellipse2D.Double(-r, -r, r * 2, r * 2));
                    }
                    break;
                }
                case "Ember": {
                    g.setStroke(new BasicStroke(2.0f));
                    for (int i = 0; i < 8; i++) {
                        double y = 30 - ((rotationAngle * 40 + i * 10) % 60);
                        double x = Math.sin(rotationAngle * 3 + i) * 18;
                        g.fill(new Ellipse2D.Double(x - 2, y - 2, 4, 4));
                    }
                    break;
                }
                case "Gilded": {
                    g.setStroke(new BasicStroke(2.0f));
                    for (int i = 0; i < 6; i++) {
                        double a = rotationAngle * 1.5 + (i * Math.PI / 3);
                        int x = (int) (Math.cos(a) * 35);
                        int y = (int) (Math.sin(a) * 20) + 5;
                        g.drawRect(x - 3, y - 3, 6, 6);
                    }
                    break;
                }
                case "Toxic": {
                    g.setStroke(new BasicStroke(2.0f));
                    for (int i = 0; i < 6; i++) {
                        double r = 10 + Math.sin(rotationAngle * 3 + i) * 25;
                        double a = i * Math.PI / 3;
                        g.draw(new Ellipse2D.Double(Math.cos(a) * r - 4, Math.sin(a) * r - 4, 8, 8));
                    }
                    break;
                }
                case "Epic": {
                    g.setStroke(new BasicStroke(2.0f));
                    double pulse = 30 + Math.sin(rotationAngle * 5) * 8;
                    for (int i = 0; i < 8; i++) {
                        double a = i * (Math.PI / 4) + rotationAngle * 0.5;
                        g.draw(new Line2D.Double(Math.cos(a) * 12, Math.sin(a) * 12, Math.cos(a) * pulse, Math.sin(a) * pulse));
                    }
                    break;
                }
                case "Magnetic": {
                    g.setStroke(new BasicStroke(1.8f));
                    for (int i = 0; i < 4; i++) {
                        double a = rotationAngle * (i % 2 == 0 ? 2 : -2);
                        g.draw(new Arc2D.Double(-35, -20, 70, 40, (int) Math.toDegrees(a), 120, Arc2D.OPEN));
                    }
                    break;
                }
                case "Glacier": {
                    g.setStroke(new BasicStroke(1.5f));
                    for (int i = 0; i < 6; i++) {
                        double a = rotationAngle + (i * Math.PI / 3);
                        double x = Math.cos(a) * 40;
                        double y = Math.sin(a) * 40;
                        g.draw(new Line2D.Double(x - 4, y, x + 4, y));
                        g.draw(new Line2D.Double(x, y - 4, x, y + 4));
                    }
                    break;
                }
                case "Volt": {
                    g.setStroke(new BasicStroke(2.0f));
                    for (int i = 0; i < 4; i++) {
                        int x1 = rand.nextInt(50) - 25;
                        int y1 = rand.nextInt(60) - 30;
                        int x2 = x1 + rand.nextInt(16) - 8;
                        int y2 = y1 + rand.nextInt(16) - 8;
                        g.draw(new Line2D.Double(x1, y1, x2, y2));
                    }
                    break;
                }
                case "Solar": {
                    g.setStroke(new BasicStroke(2.0f));
                    g.draw(new Ellipse2D.Double(-30, -30, 60, 60));
                    for (int i = 0; i < 12; i++) {
                        double a = i * Math.PI / 6 + rotationAngle;
                        g.draw(new Line2D.Double(Math.cos(a) * 30, Math.sin(a) * 30, Math.cos(a) * 42, Math.sin(a) * 42));
                    }
                    break;
                }
                case "Radiant": {
                    g.setStroke(new BasicStroke(2.5f));
                    for (int i = 0; i < 9; i++) {
                        double a = Math.PI + (i * Math.PI / 8) + Math.sin(rotationAngle * 2) * 0.1;
                        g.draw(new Line2D.Double(Math.cos(a) * 12, -24 + Math.sin(a) * 12, Math.cos(a) * 45, -24 + Math.sin(a) * 45));
                    }
                    break;
                }
                case "Mirage": {
                    g.setStroke(new BasicStroke(1.5f));
                    for (int i = -2; i <= 2; i++) {
                        double shift = Math.sin(rotationAngle * 2 + i) * 6;
                        g.draw(new Rectangle2D.Double(-20 + shift, i * 15 - 10, 40, 10));
                    }
                    break;
                }
                case "Supernova": {
                    g.setStroke(new BasicStroke(2.5f));
                    double r = 15 + ((rotationAngle * 40) % 45);
                    g.draw(new Ellipse2D.Double(-r, -r, r * 2, r * 2));
                    for (int i = 0; i < 4; i++) {
                        double a = rotationAngle * 3 + (i * Math.PI / 2);
                        g.fill(new Ellipse2D.Double(Math.cos(a) * 42 - 4, Math.sin(a) * 25 - 4, 8, 8));
                    }
                    break;
                }
                case "Inferno": {
                    g.setStroke(new BasicStroke(2.5f));
                    for (int i = 0; i < 10; i++) {
                        double a = rotationAngle * 4 + i;
                        double r = 25 + Math.sin(rotationAngle * 5 + i) * 15;
                        g.draw(new Line2D.Double(0, 10, Math.cos(a) * r, -Math.abs(Math.sin(a) * r) - 10));
                    }
                    break;
                }
                case "Legendary": {
                    g.setStroke(new BasicStroke(2.0f));
                    for (int y = -40; y <= 30; y += 6) {
                        double x1 = Math.sin(rotationAngle * 3 + y * 0.1) * 28;
                        g.fill(new Ellipse2D.Double(x1 - 2, y - 2, 4, 4));
                        g.fill(new Ellipse2D.Double(-x1 - 2, y - 2, 4, 4));
                    }
                    break;
                }
                case "Vortex": {
                    g.setStroke(new BasicStroke(2.0f));
                    for (int i = 0; i < 3; i++) {
                        double a = rotationAngle * 3 + i * 2;
                        g.draw(new Arc2D.Double(-40, -40, 80, 80, (int) Math.toDegrees(a), 160, Arc2D.OPEN));
                    }
                    break;
                }
                case "Celestial": {
                    g.setStroke(new BasicStroke(1.8f));
                    for (int t = 0; t < 2; t++) {
                        Path2D triangle = new Path2D.Double();
                        double baseA = rotationAngle * (t == 0 ? 1 : -1);
                        for (int i = 0; i < 3; i++) {
                            double a = baseA + (i * 2 * Math.PI / 3);
                            if (i == 0) triangle.moveTo(Math.cos(a) * 38, Math.sin(a) * 38);
                            else triangle.lineTo(Math.cos(a) * 38, Math.sin(a) * 38);
                        }
                        triangle.closePath();
                        g.draw(triangle);
                    }
                    break;
                }
                case "Specter": {
                    g.setStroke(new BasicStroke(1.5f));
                    for (int i = 0; i < 5; i++) {
                        double y = -40 + i * 18;
                        double x = Math.sin(rotationAngle * 2 + i) * 25;
                        g.draw(new Ellipse2D.Double(x - 15, y - 6, 30, 12));
                    }
                    break;
                }
                case "Voidborn": {
                    g.setStroke(new BasicStroke(3.0f));
                    for (int i = 0; i < 5; i++) {
                        double a = rotationAngle * 1.5 + (i * Math.PI * 2 / 5);
                        Path2D tentacle = new Path2D.Double();
                        tentacle.moveTo(0, 0);
                        tentacle.quadTo(Math.cos(a) * 30, Math.sin(a) * 15, Math.cos(a + 0.8) * 50, Math.sin(a + 0.8) * 50);
                        g.draw(tentacle);
                    }
                    break;
                }
                case "Superconductor": {
                    g.setStroke(new BasicStroke(2.0f));
                    g.draw(new Ellipse2D.Double(-45, -45, 90, 90));
                    for (int i = 0; i < 4; i++) {
                        double a = rotationAngle * 5 + i * Math.PI / 2;
                        g.fill(new Ellipse2D.Double(Math.cos(a) * 45 - 4, Math.sin(a) * 45 - 4, 8, 8));
                    }
                    break;
                }
                case "Singularity": {
                    g.setStroke(new BasicStroke(2.0f));
                    g.setColor(Color.BLACK);
                    g.fill(new Ellipse2D.Double(-20, -20, 40, 40));
                    g.setColor(color);
                    g.draw(new Ellipse2D.Double(-22, -22, 44, 44));
                    for (int i = 0; i < 8; i++) {
                        double dist = 25 + ((i * 12 + rotationAngle * 30) % 40);
                        double a = rotationAngle * 2 + i;
                        g.draw(new Line2D.Double(Math.cos(a) * dist, Math.sin(a) * dist, Math.cos(a) * (dist - 6), Math.sin(a) * (dist - 6)));
                    }
                    break;
                }
                case "Eclipse": {
                    g.setStroke(new BasicStroke(4.0f));
                    g.draw(new Ellipse2D.Double(-38, -38, 76, 76));
                    g.setColor(Color.BLACK);
                    g.fill(new Ellipse2D.Double(-34, -34, 68, 68));
                    g.setColor(color);
                    for (int i = 0; i < 12; i++) {
                        double a = i * Math.PI / 6 + rotationAngle;
                        double len = 38 + Math.sin(rotationAngle * 4 + i) * 8;
                        g.draw(new Line2D.Double(Math.cos(a) * 38, Math.sin(a) * 38, Math.cos(a) * len, Math.sin(a) * len));
                    }
                    break;
                }
                case "Hyperdrive": {
                    g.setStroke(new BasicStroke(2.0f));
                    for (int i = 0; i < 12; i++) {
                        double a = i * Math.PI / 6;
                        double start = ((rotationAngle * 60 + i * 10) % 50);
                        g.draw(new Line2D.Double(Math.cos(a) * start, Math.sin(a) * start, Math.cos(a) * (start + 15), Math.sin(a) * (start + 15)));
                    }
                    break;
                }
                case "Supergiant": {
                    g.setStroke(new BasicStroke(3.0f));
                    double pulse = 45 + Math.sin(rotationAngle * 2) * 10;
                    g.draw(new Ellipse2D.Double(-pulse, -pulse, pulse * 2, pulse * 2));
                    break;
                }
                case "Chronos": {
                    g.setStroke(new BasicStroke(2.0f));
                    g.draw(new Ellipse2D.Double(-45, -45, 90, 90));
                    g.draw(new Line2D.Double(0, 0, Math.cos(rotationAngle * 0.8) * 22, Math.sin(rotationAngle * 0.8) * 22));
                    g.draw(new Line2D.Double(0, 0, Math.cos(rotationAngle * 4.0) * 35, Math.sin(rotationAngle * 4.0) * 35));
                    for (int i = 0; i < 12; i++) {
                        double a = i * Math.PI / 6;
                        g.draw(new Line2D.Double(Math.cos(a) * 40, Math.sin(a) * 40, Math.cos(a) * 45, Math.sin(a) * 45));
                    }
                    break;
                }
                case "Overlord": {
                    g.setStroke(new BasicStroke(3.0f));
                    Path2D crown = new Path2D.Double();
                    crown.moveTo(-20, -35);
                    crown.lineTo(-20, -50); crown.lineTo(-10, -40);
                    crown.lineTo(0, -55); crown.lineTo(10, -40);
                    crown.lineTo(20, -50); crown.lineTo(20, -35);
                    crown.closePath();
                    g.draw(crown);
                    break;
                }
                case "Gargantua": {
                    g.setStroke(new BasicStroke(3.0f));
                    g.rotate(0.4);
                    g.draw(new Ellipse2D.Double(-55, -18, 110, 36));
                    g.rotate(-0.8);
                    g.draw(new Ellipse2D.Double(-55, -18, 110, 36));
                    g.rotate(0.4);
                    break;
                }
                case "NEBULA": {
                    g.setStroke(new BasicStroke(1.5f));
                    for (int i = 0; i < 16; i++) {
                        double a = rotationAngle + (i * Math.PI / 8);
                        double r = 20 + Math.sin(rotationAngle * 2 + i) * 25;
                        g.draw(new Ellipse2D.Double(Math.cos(a) * r - 3, Math.sin(a) * r - 3, 6, 6));
                    }
                    break;
                }
                case "Quantum": {
                    g.setStroke(new BasicStroke(1.5f));
                    for (int i = 0; i < 8; i++) {
                        double a = rotationAngle * 3 + i;
                        double x = Math.cos(a) * 35;
                        double y = Math.sin(a * 2) * 20;
                        g.drawRect((int) x - 3, (int) y - 3, 6, 6);
                    }
                    break;
                }
                case "ABSOLUTE ZERO": {
                    g.setStroke(new BasicStroke(2.0f));
                    Path2D frost = new Path2D.Double();
                    for (int i = 0; i < 12; i++) {
                        double a = i * Math.PI / 6 + rotationAngle * 0.5;
                        double r = (i % 2 == 0) ? 52 : 28;
                        if (i == 0) frost.moveTo(Math.cos(a) * r, Math.sin(a) * r);
                        else frost.lineTo(Math.cos(a) * r, Math.sin(a) * r);
                    }
                    frost.closePath();
                    g.draw(frost);
                    break;
                }
                case "SUPERPOSITION": {
                    g.setStroke(new BasicStroke(1.8f));
                    g.draw(new Rectangle2D.Double(-35, -35, 70, 70));
                    g.rotate(rotationAngle);
                    g.draw(new Rectangle2D.Double(-35, -35, 70, 70));
                    g.rotate(-rotationAngle);
                    break;
                }
                case "INFINITY": {
                    g.setStroke(new BasicStroke(3.0f));
                    Path2D inf = new Path2D.Double();
                    boolean first = true;
                    for (double t = 0; t <= Math.PI * 2; t += 0.1) {
                        double x = (45 * Math.cos(t)) / (1 + Math.sin(t) * Math.sin(t));
                        double y = (45 * Math.sin(t) * Math.cos(t)) / (1 + Math.sin(t) * Math.sin(t));
                        double rx = x * Math.cos(rotationAngle) - y * Math.sin(rotationAngle);
                        double ry = x * Math.sin(rotationAngle) + y * Math.cos(rotationAngle);
                        if (first) { inf.moveTo(rx, ry); first = false; }
                        else { inf.lineTo(rx, ry); }
                    }
                    g.draw(inf);
                    break;
                }
                case "DARK MATTER": {
                    g.setStroke(new BasicStroke(3.0f));
                    g.setColor(new Color(30, 10, 60));
                    g.fill(new Ellipse2D.Double(-40, -40, 80, 80));
                    g.setColor(color);
                    for (int i = 0; i < 6; i++) {
                        double a = rotationAngle * 2 + i;
                        g.draw(new Line2D.Double(Math.cos(a) * 40, Math.sin(a) * 40, Math.cos(a) * 55, Math.sin(a) * 55));
                    }
                    break;
                }
                case "THE OMNIPRESENT": {
                    g.setStroke(new BasicStroke(2.0f));
                    g.draw(new Ellipse2D.Double(-50, -50, 100, 100));
                    g.rotate(rotationAngle * 2);
                    g.draw(new Ellipse2D.Double(-50, -20, 100, 40));
                    g.rotate(-rotationAngle * 4);
                    g.draw(new Ellipse2D.Double(-20, -50, 40, 100));
                    g.rotate(rotationAngle * 2);
                    break;
                }
                case "BIG BANG": {
                    g.setStroke(new BasicStroke(2.5f));
                    double r = 10 + ((rotationAngle * 80) % 65);
                    g.draw(new Ellipse2D.Double(-r, -r, r * 2, r * 2));
                    break;
                }
                case "CREATOR": {
                    g.setStroke(new BasicStroke(3.0f));
                    g.draw(new Ellipse2D.Double(-58, -58, 116, 116));
                    for (int i = 0; i < 12; i++) {
                        double a = rotationAngle * 2 + (i * Math.PI / 6);
                        g.fill(new Ellipse2D.Double(Math.cos(a) * 58 - 4, Math.sin(a) * 58 - 4, 8, 8));
                    }
                    break;
                }
                case "MULTIVERSE": {
                    g.setStroke(new BasicStroke(2.0f));
                    for (int i = 0; i < 5; i++) {
                        double a = rotationAngle + i * Math.PI * 2 / 5;
                        g.draw(new Ellipse2D.Double(Math.cos(a) * 35 - 15, Math.sin(a) * 35 - 15, 30, 30));
                    }
                    break;
                }
                case "ARCHITECT": {
                    g.setStroke(new BasicStroke(2.0f));
                    for (int i = 0; i < 4; i++) {
                        g.rotate(rotationAngle + i * Math.PI / 2);
                        g.draw(new Rectangle2D.Double(-25, -25, 50, 50));
                        g.rotate(-(rotationAngle + i * Math.PI / 2));
                    }
                    break;
                }
                case "EXISTENCE": {
                    g.setStroke(new BasicStroke(2.5f));
                    double r = 45 + Math.sin(rotationAngle * 3) * 15;
                    g.draw(new Ellipse2D.Double(-r, -r, r * 2, r * 2));
                    break;
                }
                case "ZENITH": {
                    g.setStroke(new BasicStroke(3.0f));
                    g.draw(new Ellipse2D.Double(-60, -60, 120, 120));
                    g.setColor(Color.WHITE);
                    g.fill(new Ellipse2D.Double(-10, -10, 20, 20));
                    break;
                }
                default:
                    break;
            }
        }

        private void drawStickFigure(Graphics2D g, String pose) {
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            // Head
            g.draw(new Ellipse2D.Double(-8, -32, 16, 16));
            // Body
            g.draw(new Line2D.Double(0, -16, 0, 12));

            // Legs
            if (pose.equals("FLOAT")) {
                g.draw(new Line2D.Double(0, 12, -7, 28));
                g.draw(new Line2D.Double(0, 12, 7, 28));
            } else {
                g.draw(new Line2D.Double(0, 12, -10, 30));
                g.draw(new Line2D.Double(0, 12, 10, 30));
            }

            // Arms / Pose
            if (pose.equals("FLEX")) {
                g.draw(new Line2D.Double(0, -10, -12, -10));
                g.draw(new Line2D.Double(-12, -10, -12, -22));
                g.draw(new Line2D.Double(0, -10, 12, -10));
                g.draw(new Line2D.Double(12, -10, 12, -22));
            } else if (pose.equals("POWER")) {
                g.draw(new Line2D.Double(0, -10, -16, 2));
                g.draw(new Line2D.Double(0, -10, 16, 2));
            } else if (pose.equals("FLOAT")) {
                g.draw(new Line2D.Double(0, -10, -18, -10));
                g.draw(new Line2D.Double(0, -10, 18, -10));
            } else if (pose.equals("HERO")) {
                g.draw(new Line2D.Double(0, -10, -10, -2));
                g.draw(new Line2D.Double(-10, -2, 0, 4));
                g.draw(new Line2D.Double(0, -10, 10, -2));
                g.draw(new Line2D.Double(10, -2, 0, 4));
            } else {
                g.draw(new Line2D.Double(0, -10, -8, 6));
                g.draw(new Line2D.Double(0, -10, 8, 6));
            }
        }
    }

    // ---------------------------------------------------------
    // 4. CORE GENERATOR PANEL (ROLL FX + SHAKE)
    // ---------------------------------------------------------
    private class CorePanel extends JPanel {
        private double angle = 0;
        private final Random rand = new Random();

        public CorePanel() {
            setBackground(new Color(17, 24, 39));
            setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(31, 41, 55), 1),
                    "CORE SYNTHESIZER",
                    0, 0,
                    new Font("Monospaced", Font.BOLD, 10),
                    new Color(148, 163, 184)
            ));
        }

        public void updateAnimation() {
            angle += isRolling ? 0.18 : 0.03;

            if (popScale > 1.0f) {
                popScale -= 0.04f;
                if (popScale < 1.0f) popScale = 1.0f;
            }

            if (flashOpacity > 0.0f) {
                flashOpacity -= 0.05f;
                if (flashOpacity < 0.0f) flashOpacity = 0.0f;
            }

            if (shakeIntensity > 0) {
                shakeIntensity--;
            }

            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int offsetX = 0;
            int offsetY = 0;
            if (shakeIntensity > 0) {
                offsetX = rand.nextInt(shakeIntensity * 2 + 1) - shakeIntensity;
                offsetY = rand.nextInt(shakeIntensity * 2 + 1) - shakeIntensity;
            }

            int cx = getWidth() / 2 + offsetX;
            int cy = getHeight() / 2 + offsetY;

            Aura currentAura = AURAS.getOrDefault(currentAuraName, AURAS.get("Common"));
            Color color = currentAura.color;

            g2d.translate(cx, cy);
            g2d.scale(popScale, popScale);

            // Glow Backplate
            float[] dist = {0.0f, 1.0f};
            Color[] colors = {color, new Color(0, 0, 0, 0)};
            RadialGradientPaint p = new RadialGradientPaint(0, 0, 75, dist, colors);
            g2d.setPaint(p);
            g2d.fill(new Ellipse2D.Double(-75, -75, 150, 150));

            // Dynamic Shapes
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(2.5f));

            String style = currentAura.shapeStyle;
            int count = currentAura.particleCount;

            if (style.equals("CIRCLE")) {
                g2d.draw(new Ellipse2D.Double(-28, -28, 56, 56));
            } else if (style.equals("RING") || style.equals("ORBIT")) {
                g2d.rotate(angle);
                g2d.draw(new Ellipse2D.Double(-45, -16, 90, 32));
                g2d.rotate(-angle);
            } else if (style.equals("SPIKES") || style.equals("STARBURST")) {
                g2d.rotate(angle);
                for (int i = 0; i < count; i++) {
                    g2d.rotate((Math.PI * 2) / count);
                    g2d.draw(new Line2D.Double(0, 0, 0, 50));
                }
                g2d.rotate(-angle);
            } else {
                g2d.rotate(-angle);
                for (int i = 0; i < count; i++) {
                    g2d.rotate((Math.PI * 2) / count);
                    g2d.draw(new Rectangle2D.Double(-20, -20, 40, 40));
                }
                g2d.rotate(angle);
            }

            // Core Energy Center
            g2d.setColor(Color.WHITE);
            g2d.fill(new Ellipse2D.Double(-8, -8, 16, 16));

            // Screen Flash overlay
            if (flashOpacity > 0.0f) {
                g2d.scale(1.0 / popScale, 1.0 / popScale);
                g2d.setColor(new Color(1.0f, 1.0f, 1.0f, flashOpacity));
                g2d.fillRect(-cx, -cy, getWidth(), getHeight());
            }

            g2d.dispose();
        }
    }

    // ---------------------------------------------------------
    // 5. GAME TIMERS & RNG SELECTION
    // ---------------------------------------------------------
    private void startTimers() {
        javax.swing.Timer renderTimer = new javax.swing.Timer(16, e -> {
            figurePanel.updateAnimation();
            corePanel.updateAnimation();
        });
        renderTimer.start();

        javax.swing.Timer weatherTimerObj = new javax.swing.Timer(1000, e -> {
            weatherTimer--;
            if (weatherTimer <= 0) {
                currentWeather = WEATHER_EVENTS.get(new Random().nextInt(WEATHER_EVENTS.size()));
                weatherTimer = 10;
                updateLuckLabel();
            }
            lblWeather.setText(String.format("EVENT: %s (%.1fx) | Resets in %ds", currentWeather.name, currentWeather.multiplier, weatherTimer));
            lblWeather.setForeground(currentWeather.color);
        });
        weatherTimerObj.start();
    }

    private double getTotalLuck() {
        double rebirthLuckBonus = 1.0 + (rebirthTokens * 2.5);
        return baseLuck * currentWeather.multiplier * rebirthLuckBonus;
    }

    private double getRebirthShardBonusMultiplier() {
        return 1.0 + (rebirthTokens * 0.5);
    }

    private void startRollSequence() {
        if (isRolling) return;
        isRolling = true;
        btnRoll.setEnabled(false);

        double effectiveLuck = getTotalLuck();
        String rolledAura = "Common";

        List<Aura> sorted = new ArrayList<>(AURAS.values());
        sorted.sort((a, b) -> Double.compare(a.chance, b.chance));

        Random rand = new Random();
        for (Aura a : sorted) {
            if (rand.nextDouble() < (a.chance * effectiveLuck)) {
                rolledAura = a.name;
                break;
            }
        }

        final String finalAura = rolledAura;
        final List<String> names = new ArrayList<>(AURAS.keySet());
        final int[] ticks = {0};

        lblStatus.setText("⚡ SYNTHESIZING CORE...");
        lblStatus.setForeground(new Color(250, 204, 21));

        javax.swing.Timer spinTimer = new javax.swing.Timer(40, null);
        spinTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentAuraName = names.get(rand.nextInt(names.size()));
                lblAuraName.setText("???");
                lblAuraName.setForeground(new Color(148, 163, 184));
                ticks[0]++;

                if (ticks[0] > 14) {
                    spinTimer.stop();
                    finalizeRoll(finalAura);
                }
            }
        });
        spinTimer.start();
    }

    private void finalizeRoll(String auraName) {
        totalRolls++;
        currentAuraName = auraName;
        Aura data = AURAS.get(auraName);

        long baseGainedShards = data.value * shardMultiplierLevel;
        long gainedShards = Math.round(baseGainedShards * getRebirthShardBonusMultiplier());

        inventory.put(auraName, inventory.getOrDefault(auraName, 0L) + 1);
        shards += gainedShards;

        popScale = 1.35f;
        flashOpacity = 0.55f;

        long rarityInverse = (long) (1.0 / data.chance);
        if (rarityInverse >= 100000) shakeIntensity = 20;
        else if (rarityInverse >= 1000) shakeIntensity = 12;
        else shakeIntensity = 4;

        lblRolls.setText("ROLLS: " + String.format("%,d", totalRolls));
        lblShards.setText("SHARDS: " + String.format("%,d", shards));

        lblStatus.setText("✦ ITEM OBTAINED! ✦");
        lblStatus.setForeground(data.color);
        lblAuraName.setText(auraName.toUpperCase());
        lblAuraName.setForeground(data.color);
        lblOdds.setText(String.format("Rarity: 1 in %,d | Reward: +%,d Shards", rarityInverse, gainedShards));

        updateInventoryUI();
        isRolling = false;
        btnRoll.setEnabled(true);

        if (autoActive) {
            javax.swing.Timer autoDelay = new javax.swing.Timer(autoDelayMs, e -> startRollSequence());
            autoDelay.setRepeats(false);
            autoDelay.start();
        }
    }

    private void toggleAuto() {
        autoActive = !autoActive;
        if (autoActive) {
            btnAuto.setText("⚙ AUTO: ON");
            btnAuto.setBackground(new Color(74, 222, 128));
            btnAuto.setForeground(Color.BLACK);
            if (!isRolling) startRollSequence();
        } else {
            btnAuto.setText("⚙ AUTO: OFF");
            btnAuto.setBackground(new Color(239, 68, 68));
            btnAuto.setForeground(Color.WHITE);
        }
    }

    // ---------------------------------------------------------
    // 6. UPGRADES & PROGRESSIVE REBIRTH
    // ---------------------------------------------------------
    private long calculateNextRebirthCost() {
        return (long) (1000000L * Math.pow(2.5, rebirthTokens));
    }

    private void updateRebirthButtonText() {
        long cost = calculateNextRebirthCost();
        btnRebirth.setText(String.format("🌀 REBIRTH (%,d S)", cost));
    }

    private void buyLensUpgrade() {
        if (shards >= lensUpgradeCost) {
            shards -= lensUpgradeCost;
            baseLuck += 0.5;
            lensUpgradeCost = (long) (lensUpgradeCost * 1.6);

            lblShards.setText("SHARDS: " + String.format("%,d", shards));
            updateLuckLabel();
            btnUpgradeLens.setText(String.format("Lens (+0.5x) | %,d S", lensUpgradeCost));
        } else {
            JOptionPane.showMessageDialog(this, "Not enough shards!", "Notice", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void buySpeedUpgrade() {
        if (autoDelayMs <= 40) {
            JOptionPane.showMessageDialog(this, "Auto-Roll speed is already at MAX!", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (shards >= speedUpgradeCost) {
            shards -= speedUpgradeCost;
            autoDelayMs = Math.max(40, autoDelayMs - 40);
            speedUpgradeCost = (long) (speedUpgradeCost * 2.2);

            lblShards.setText("SHARDS: " + String.format("%,d", shards));
            btnUpgradeSpeed.setText(autoDelayMs <= 40 ? "Speed (MAX)" : String.format("Auto Speed | %,d S", speedUpgradeCost));
        } else {
            JOptionPane.showMessageDialog(this, "Not enough shards!", "Notice", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void buyShardUpgrade() {
        if (shards >= shardUpgradeCost) {
            shards -= shardUpgradeCost;
            shardMultiplierLevel++;
            shardUpgradeCost = (long) (shardUpgradeCost * 2.0);

            lblShards.setText("SHARDS: " + String.format("%,d", shards));
            btnUpgradeShard.setText(String.format("Shard Gain (+1x) | %,d S", shardUpgradeCost));
        } else {
            JOptionPane.showMessageDialog(this, "Not enough shards!", "Notice", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void performRebirth() {
        long rebirthCost = calculateNextRebirthCost();
        if (shards < rebirthCost) {
            double nextLuckBonus = 250.0;
            double nextShardBonus = 50.0;
            long startingBonusShards = (long) (1000 * Math.pow(rebirthTokens + 1, 1.8));

            String msg = String.format(
                    "Rebirth requires %,d Shards!\n\n" +
                            "NEXT REBIRTH BUFFS:\n" +
                            "• +%.0f%% Global Luck Multiplier\n" +
                            "• +%.0f%% Shards Earned Multiplier\n" +
                            "• Starting Shards: %,d S\n" +
                            "• Permanent Speed Retention Upgrade",
                    rebirthCost, nextLuckBonus, nextShardBonus, startingBonusShards);

            JOptionPane.showMessageDialog(this, msg, "Insufficient Funds", JOptionPane.WARNING_MESSAGE);
            return;
        }

        long nextRebirthNum = rebirthTokens + 1;
        long startingShards = (long) (1000 * Math.pow(nextRebirthNum, 1.8));

        int confirm = JOptionPane.showConfirmDialog(this,
                String.format("Rebirth #%d costs %,d Shards.\n\n" +
                                "PERKS UNLOCKED:\n" +
                                "✦ Global Luck Bonus: +%.0f%%\n" +
                                "✦ Permanent Shard Multiplier: +%.0f%%\n" +
                                "✦ Fast-Start Bonus: Start with %,d Shards\n\n" +
                                "Reset Shards & base upgrades now?",
                        nextRebirthNum, rebirthCost, (nextRebirthNum * 250.0), (nextRebirthNum * 50.0), startingShards),
                "Confirm Rebirth", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            rebirthTokens += 1;

            int currentSpeedUpgrades = (300 - autoDelayMs) / 40;
            int retainedSpeedUpgrades = Math.min(6, (int) (currentSpeedUpgrades * 0.4)); 

            shards = (long) (1000 * Math.pow(rebirthTokens, 1.8));
            baseLuck = 1.0;
            lensUpgradeCost = 50;

            autoDelayMs = Math.max(40, 300 - (retainedSpeedUpgrades * 40));
            speedUpgradeCost = 250;

            shardMultiplierLevel = 1;
            shardUpgradeCost = 500;

            lblShards.setText("SHARDS: " + String.format("%,d", shards));
            lblRebirths.setText("REBIRTHS: " + rebirthTokens);
            btnUpgradeLens.setText("Lens (+0.5x) | 50 S");
            btnUpgradeSpeed.setText(autoDelayMs <= 40 ? "Speed (MAX)" : String.format("Auto Speed | %,d S", speedUpgradeCost));
            btnUpgradeShard.setText("Shard Gain (+1x) | 500 S");

            updateRebirthButtonText();
            updateLuckLabel();

            JOptionPane.showMessageDialog(this,
                    String.format("🎉 REBIRTH #%d ACHIEVED!\n" +
                                    "Your progressive passive perks are now active!", rebirthTokens),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateLuckLabel() {
        lblLuck.setText(String.format("LUCK: %.2fx", getTotalLuck()));
    }

    // ---------------------------------------------------------
    // 7. AURA INDEX / CODEX UI SYSTEM
    // ---------------------------------------------------------
    private void updateInventoryUI() {
        invPanel.removeAll();
        int discoveredCount = 0;

        for (Map.Entry<String, Aura> entry : AURAS.entrySet()) {
            String name = entry.getKey();
            Aura data = entry.getValue();
            long count = inventory.getOrDefault(name, 0L);
            boolean unlocked = count > 0;

            if (unlocked) discoveredCount++;

            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(new Color(17, 24, 39));
            row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(31, 41, 55)));

            long rarityInverse = (long) (1.0 / data.chance);

            if (unlocked) {
                JLabel nameLbl = new JLabel(String.format("  ● %s (1 in %,d)%s", name, rarityInverse, (name.equals(equippedAuraName) ? " [EQUIPPED]" : "")));
                nameLbl.setFont(new Font("Monospaced", Font.BOLD, 10));
                nameLbl.setForeground(data.color);

                JButton equipBtn = new JButton(name.equals(equippedAuraName) ? "EQUIPPED" : "EQUIP");
                equipBtn.setFont(new Font("Monospaced", Font.BOLD, 9));
                equipBtn.setBackground(name.equals(equippedAuraName) ? new Color(34, 197, 94) : new Color(31, 41, 55));
                equipBtn.setForeground(Color.WHITE);
                equipBtn.setFocusPainted(false);
                equipBtn.addActionListener(e -> {
                    equippedAuraName = name;
                    lblEquipped.setText("EQUIPPED: " + equippedAuraName.toUpperCase());
                    lblEquipped.setForeground(AURAS.get(equippedAuraName).color);
                    updateInventoryUI();
                });

                JLabel countLbl = new JLabel("x" + String.format("%,d ", count));
                countLbl.setFont(new Font("Monospaced", Font.PLAIN, 10));
                countLbl.setForeground(Color.WHITE);

                JPanel rightSide = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 1));
                rightSide.setOpaque(false);
                rightSide.add(countLbl);
                rightSide.add(equipBtn);

                row.add(nameLbl, BorderLayout.WEST);
                row.add(rightSide, BorderLayout.EAST);
            } else {
                JLabel nameLbl = new JLabel("  🔒 ⛓⛓ [LOCKED AURA] ⛓⛓");
                nameLbl.setFont(new Font("Monospaced", Font.ITALIC, 10));
                nameLbl.setForeground(new Color(75, 85, 99));

                JLabel lockLbl = new JLabel("??? ");
                lockLbl.setFont(new Font("Monospaced", Font.BOLD, 9));
                lockLbl.setForeground(new Color(75, 85, 99));

                row.add(nameLbl, BorderLayout.WEST);
                row.add(lockLbl, BorderLayout.EAST);
            }

            invPanel.add(row);
        }

        if (lblIndexHeader != null) {
            lblIndexHeader.setText("📖 AURA INDEX / CODEX (DISCOVERED: " + discoveredCount + " / " + AURAS.size() + ")");
        }

        invPanel.revalidate();
        invPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AuraGame game = new AuraGame();
            game.setVisible(true);
        });
    }
}