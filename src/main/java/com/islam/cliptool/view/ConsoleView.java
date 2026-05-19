/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliptool.view;

import com.islam.cliptool.model.Subtitle;
import com.islam.cliptool.table.SubtitleTableModel;
import com.islam.cliptool.util.SrtParser;
import com.islam.cliptool.util.SrtWriter;
import javax.swing.JViewport;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
/**
 *
 * @author i3akk
 */
public class ConsoleView extends JFrame{
    EmbeddedMediaPlayerComponent videoComponent = new EmbeddedMediaPlayerComponent();
    JTextArea area = new JTextArea();;
    JTabbedPane tb = new JTabbedPane();;
    JToolBar bar = new JToolBar();;
    JButton newButton = new JButton("New");
    JButton openButton = new JButton("Open");
    JButton saveButton = new JButton("Save");

    
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenu videoMenu = new JMenu("Video");
    JMenu themesMenu = new JMenu("Themes");
    JMenu helpMenu = new JMenu("Help");
    JMenu consoleThemeItem = new JMenu("Console Themes");
    JMenu playerThemeItem = new JMenu("Player Themes");
    
    JMenuItem exitItem = new JMenuItem("Exit");
    JMenuItem openVideoItem = new JMenuItem("Open Video");
    
    JMenuItem helpItem = new JMenuItem("about");
    JMenuItem nativeItem = new JMenuItem("Native Theme");
    JMenuItem draculaItem = new JMenuItem("Dracula Theme");

    JLabel startTime = new JLabel("00:00:00");
    JLabel endTime = new JLabel("00:00:00");
    
    boolean isSeeking = false;
    private boolean syncing = false;
    private int currentSubtitleRow = -1;
    SubtitleTableModel model = new SubtitleTableModel();
    JTable table = new JTable(model);
    JPanel emptyPanel = new JPanel(new BorderLayout());
    JLabel volumeLabel = new JLabel();
    private volatile boolean userSeeking = false;
    private boolean autoFollowSubtitle = true;
    public ConsoleView() {
        javax.swing.UIManager.put(
            "Button.defaultButtonFollowsFocus",
            Boolean.TRUE
        );

        volumeLabel.setBackground(new Color(0, 0, 0, 0));
        volumeLabel.setForeground(Color.WHITE);
        volumeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        volumeLabel.setOpaque(false); // no gray background
        volumeLabel.setVisible(false);
        table.setDragEnabled(true);
        table.setDropMode(javax.swing.DropMode.INSERT);
        table.setDropTarget(new java.awt.dnd.DropTarget());
        table.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY));
        table.getTableHeader().setResizingAllowed(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(0).setMinWidth(30);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setMinWidth(55);
        table.getColumnModel().getColumn(1).setMaxWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        
        table.getColumnModel().getColumn(2).setMinWidth(55);
        table.getColumnModel().getColumn(2).setMaxWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        
        JScrollPane JTableScroll = new JScrollPane(table);
        
        JLabel emptyLabel = new JLabel(
            "<html><pre style='font-family:monospace;'>"
            + loadAscii("/ascii/reader.txt")
            + "</pre></html>"
        );
        JPanel tableLayer = new JPanel();
        tableLayer.setLayout(new OverlayLayout(tableLayer));

        JTableScroll.setOpaque(true);
        emptyPanel.setOpaque(false);
        emptyPanel.setAlignmentX(0.5f);
        emptyPanel.setAlignmentY(0.5f);

        JTableScroll.setAlignmentX(0.5f);
        JTableScroll.setAlignmentY(0.5f);

        tableLayer.add(emptyPanel);
        tableLayer.add(JTableScroll);

        emptyLabel.setHorizontalAlignment(JLabel.CENTER);
        emptyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emptyLabel.setForeground(Color.GRAY);

        emptyPanel.add(emptyLabel);
        
        JTableHeader header = table.getTableHeader();
        
        table.getTableHeader().setPreferredSize(
            new Dimension(
                table.getTableHeader().getPreferredSize().width,
                25 // height
            )
        );
        
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                JLabel label = new JLabel(value.toString());
                label.setOpaque(true);
                label.setBackground(new Color(193, 213, 232)); 
                label.setForeground(Color.BLACK);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(BorderFactory.createMatteBorder(
                    0, 0, 0, 1, Color.LIGHT_GRAY
                ));

                return label;
            }
        });
        header.setBorder(BorderFactory.createRaisedBevelBorder());
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setGridColor(Color.LIGHT_GRAY);
        
        table.getColumnModel().getColumn(0).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            java.awt.Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                c.setBackground(new Color(201, 235, 206)); // light green
                c.setForeground(Color.BLACK);
            } else {
                c.setBackground(new Color(102, 205, 170)); // selected green-ish
                c.setForeground(Color.BLACK);
            }

            return c;
        }
    });
        
        fileMenu.add(exitItem);
        themesMenu.add(consoleThemeItem);
        themesMenu.add(playerThemeItem);
        consoleThemeItem.add(nativeItem);
        consoleThemeItem.add(draculaItem);
        
        helpMenu.add(helpItem);
        videoMenu.add(openVideoItem);
        
        menuBar.add(fileMenu);
        menuBar.add(videoMenu);
        menuBar.add(themesMenu);
        menuBar.add(helpMenu);
        area.setEditable(true);
        area.setBackground(new Color(40, 42, 54));
        area.setSelectionColor(new Color(255,255,255));
        area.setForeground(new Color(241, 250, 140));
        try (InputStream is = getClass().getResourceAsStream("/fonts/KawkabMono-Regular.ttf")) {

            Font customFont = Font.createFont(Font.TRUETYPE_FONT, is)
                                  .deriveFont(14f);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

            area.setFont(customFont);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        bar.add(newButton);
        bar.add(openButton);
        bar.add(saveButton);
        
        JScrollPane scrollPane = new JScrollPane(area);
        tb.add(scrollPane, "Console");
        ImageIcon playIcon = new ImageIcon(
            getClass().getResource("/icons/icons8-play-16.png")
        );
        
        ImageIcon pauseIcon = new ImageIcon(
            getClass().getResource("/icons/icons8-pause-16.png")
        );
        
        ImageIcon stopIcon = new ImageIcon(
            getClass().getResource("/icons/icons8-stop-16.png")
        );
        JButton playBtn = new JButton(playIcon);
        playBtn.setFocusable(false);
        playBtn.setPreferredSize(new Dimension(23, 23));
        playBtn.setMinimumSize(new Dimension(23, 23));
        playBtn.setMaximumSize(new Dimension(23, 23));
        JButton pauseBtn = new JButton(pauseIcon);
        pauseBtn.setFocusable(false);
        pauseBtn.setPreferredSize(new Dimension(23, 23));
        pauseBtn.setMinimumSize(new Dimension(23, 23));
        pauseBtn.setMaximumSize(new Dimension(23, 23));
        JButton stopBtn = new JButton(stopIcon);
        stopBtn.setFocusable(false);
        stopBtn.setPreferredSize(new Dimension(23, 23));
        stopBtn.setMinimumSize(new Dimension(23, 23));
        stopBtn.setMaximumSize(new Dimension(23, 23));
        disableSpace(newButton);
        disableSpace(openButton);
        disableSpace(saveButton);

        disableSpace(playBtn);
        disableSpace(pauseBtn);
        disableSpace(stopBtn);
        
        enableEnter(newButton);
        enableEnter(openButton);
        enableEnter(saveButton);
        enableEnter(playBtn);
        
        enableEnter(pauseBtn);
        enableEnter(stopBtn);
        
        JSlider seekBar = new JSlider(0, 1000, 0);
        seekBar.setFocusable(false);
        seekBar.setBorder(null);
        seekBar.setOpaque(false);
        seekBar.setPaintTicks(false);
        seekBar.setPaintLabels(false);

        seekBar.setPreferredSize(new Dimension(300, 20));
        
        JPanel leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        leftControls.setOpaque(false);
        leftControls.add(playBtn);
        leftControls.add(pauseBtn);
        leftControls.add(stopBtn);
        
        JPanel centerControls = new JPanel(new BorderLayout(7, 0));
        centerControls.setOpaque(false);
        centerControls.add(startTime, BorderLayout.WEST);
        centerControls.add(seekBar, BorderLayout.CENTER);
        centerControls.add(endTime, BorderLayout.EAST);

        
        JPanel controlBar = new JPanel(new BorderLayout());
        controlBar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        controlBar.setBackground(new Color(241, 250, 140));
        controlBar.setOpaque(true);
        controlBar.add(leftControls, BorderLayout.WEST);
        controlBar.add(centerControls, BorderLayout.CENTER);
        
        JLayeredPane videoLayer = new JLayeredPane();
        videoLayer.setOpaque(false);
        videoLayer.setBackground(new Color(0, 0, 0, 0));
        videoLayer.setLayout(null);
        videoLayer.add(videoComponent, Integer.valueOf(0));
        videoLayer.add(volumeLabel, Integer.valueOf(2));

        videoComponent.setBounds(0, 0, 800, 500);
        volumeLabel.setBounds(10, 10, 80, 20);

        
        
        JPanel topLeftPanel = new JPanel(new BorderLayout());
        topLeftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Player",TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.PLAIN, 12), Color.BLACK));
        topLeftPanel.add(videoLayer, BorderLayout.CENTER);
        topLeftPanel.add(controlBar, BorderLayout.SOUTH);
        topLeftPanel.setOpaque(false);
        topLeftPanel.setVisible(false);
        JPanel BottomPanel = new JPanel(new BorderLayout());
        BottomPanel.add(tb, BorderLayout.CENTER);
        
        JPanel topRightPanel = new JPanel(new BorderLayout());
        topRightPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Reader",TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.PLAIN, 12), Color.BLACK));
        topRightPanel.add(tableLayer, BorderLayout.CENTER);
        
        JPanel wholeTopPanel = new JPanel(new BorderLayout());
        wholeTopPanel.add(topLeftPanel, BorderLayout.WEST);
        wholeTopPanel.add(topRightPanel, BorderLayout.CENTER);
        
        
        JPanel toolbarAndTopPanel = new JPanel(new BorderLayout());
        toolbarAndTopPanel.add(bar, BorderLayout.NORTH);
        toolbarAndTopPanel.add(wholeTopPanel, BorderLayout.CENTER);
        
        JPanel sumOfPanel = new JPanel(new GridLayout(2,1));
        sumOfPanel.add(toolbarAndTopPanel);
        sumOfPanel.add(BottomPanel);
                
        
        add(sumOfPanel);
        // Action listener
        exitItem.addActionListener(e -> System.exit(0));
        openVideoItem.addActionListener(e -> {

        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(
            new FileNameExtensionFilter(
                "Video Files",
                "mp4", "mkv", "avi", "mov", "wmv", "flv", "webm"
            )
        );

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {

            File selectedFile = chooser.getSelectedFile();
            videoLayer.revalidate();
            videoLayer.repaint();
            videoComponent.setBounds(
            0,
            0,
            videoLayer.getWidth(),
            videoLayer.getHeight()
        );

            topLeftPanel.setVisible(true);
            videoComponent.mediaPlayer()
                .media()
                .play(selectedFile.getAbsolutePath());
            
            SwingUtilities.invokeLater(() -> {
                loadSubtitlesIntoVlc();
            });
            
        }
    });
        
        playBtn.addActionListener(e -> {
            videoComponent.mediaPlayer().controls().play();
        });
        
        pauseBtn.addActionListener(e -> {
            videoComponent.mediaPlayer().controls().pause();
        });
        
        stopBtn.addActionListener(e -> {
            videoComponent.mediaPlayer().controls().stop();
        });
        
        seekBar.addChangeListener(e -> {
            if (seekBar.getValueIsAdjusting()) {
                isSeeking = true;

                float position = seekBar.getValue() / 1000f;
                videoComponent.mediaPlayer().controls().setPosition(position);
            }else {
                isSeeking = false;
            }

        });
        
        videoComponent.mediaPlayer().events().addMediaPlayerEventListener( new MediaPlayerEventAdapter() {

                @Override
                public void positionChanged(
                        uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer,
                        float newPosition) {

                    if (!isSeeking) {

                        SwingUtilities.invokeLater(() -> {
                            seekBar.setValue((int) (newPosition * 1000));
                        });

                    }
                }
            }
        );
        
        videoComponent.mediaPlayer().events().addMediaPlayerEventListener(
            new uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter() {

                @Override
                public void timeChanged(
                        uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer,
                        long newTime) {

                    SwingUtilities.invokeLater(() -> {

                        long total = mediaPlayer.status().length();

                        startTime.setText(formatTime(newTime));
                        endTime.setText(formatTime(total));
                        syncSubtitle(newTime);

                    });
                }
            }
        );
        
        videoComponent.addMouseWheelListener(e -> {

            int current = videoComponent.mediaPlayer().audio().volume();

            current += (e.getWheelRotation() < 0) ? 5 : -5;
            current = Math.max(0, Math.min(200, current));
            videoComponent.mediaPlayer().audio().setVolume(current);

            volumeLabel.setText("Vol " + current + "%");
            volumeLabel.setVisible(true);
            volumeLabel.setSize(volumeLabel.getPreferredSize());
            
            // hide after short delay
            new javax.swing.Timer(700, ev -> volumeLabel.setVisible(false)) {{
                setRepeats(false);
                start();
            }};
        });
        
        JScrollBar bar = JTableScroll.getVerticalScrollBar();

            bar.addAdjustmentListener(e -> {
                autoFollowSubtitle = false;
        });    
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {

                int row = table.rowAtPoint(e.getPoint());

                if (row < 0) return;

                table.setRowSelectionInterval(row, row); // force selection immediately

                Subtitle s = model.getData().get(row);

                userSeeking = true;

                videoComponent.mediaPlayer()
                        .controls()
                        .setTime(s.getStartTime());

                videoComponent.mediaPlayer()
                        .controls()
                        .play();

                new javax.swing.Timer(400, ev -> userSeeking = false) {{
                    setRepeats(false);
                    start();
                }};
            }
        });
        
        
        JTableScroll.setTransferHandler(new javax.swing.TransferHandler() {

                @Override
                public boolean canImport(TransferSupport support) {
                    return support.isDataFlavorSupported(
                            java.awt.datatransfer.DataFlavor.javaFileListFlavor
                    );
                }

                @Override
                public boolean importData(TransferSupport support) {

                    try {
                        java.util.List<File> files =
                                (java.util.List<File>) support.getTransferable()
                                        .getTransferData(
                                                java.awt.datatransfer.DataFlavor.javaFileListFlavor
                                        );

                        for (File file : files) {
                            if (file.getName().toLowerCase().endsWith(".srt")) {
                                loadSubtitles(file);
                            }
                        }

                        return true;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return false;
                }
            });
        
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {

            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                java.awt.Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (row == currentSubtitleRow) {
                    c.setBackground(new Color(255, 230, 120));
                    c.setForeground(Color.BLACK);
                } else if (isSelected) {
                    c.setBackground(Color.PINK);
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });
        
        KeyStroke spaceKey = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);

        videoComponent.getInputMap(JComponent.WHEN_FOCUSED).put(spaceKey, "togglePlayPause");

        videoComponent.getActionMap().put("togglePlayPause", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                if (videoComponent.mediaPlayer().status().isPlaying()) {
                    videoComponent.mediaPlayer().controls().pause();
                } else {
                    videoComponent.mediaPlayer().controls().play();
                }
            }
        });
        
        videoLayer.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                int w = videoLayer.getWidth();
                int h = videoLayer.getHeight();

                videoComponent.setBounds(0, 0, w, h);

                // RIGHT TOP CORNER POSITION
                int labelWidth = 80;
                int margin = 10;

                volumeLabel.setBounds(
                        w - labelWidth - margin,  // X (right side)
                        margin,                   // Y (top)
                        labelWidth,
                        20
                );

                videoLayer.revalidate();
                videoLayer.repaint();
            }
        });
        
        
        table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
            .put(
                javax.swing.KeyStroke.getKeyStroke(
                    java.awt.event.KeyEvent.VK_F,
                    java.awt.event.InputEvent.CTRL_DOWN_MASK
                ),
                "jumpToCurrentSubtitle"
            );
    
        
        table.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
         .put(
             javax.swing.KeyStroke.getKeyStroke(
                     java.awt.event.KeyEvent.VK_F,
                     java.awt.event.InputEvent.CTRL_DOWN_MASK
             ),
             "jumpToCurrentSubtitle"
         );

        table.getActionMap().put(
            "jumpToCurrentSubtitle",
            new javax.swing.AbstractAction() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {

                    if (currentSubtitleRow >= 0
                            && currentSubtitleRow < table.getRowCount()) {

                        // Select highlighted row
                        table.setRowSelectionInterval(
                                currentSubtitleRow,
                                currentSubtitleRow
                        );

                        // Get row rectangle
                        java.awt.Rectangle rect =
                                table.getCellRect(
                                        currentSubtitleRow,
                                        0,
                                        true
                                );

                        // Get viewport
                        JViewport viewport =
                                (JViewport) table.getParent();

                        // Move row to TOP
                        viewport.setViewPosition(
                                new java.awt.Point(0, rect.y)
                        );

                        table.requestFocusInWindow();
                    }
                }
            }
        );
        
        setJMenuBar(menuBar);
        setSize(950,600);
        setTitle("CliSub");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        
    }
    
    private void enableEnter(AbstractButton b) {
        b.getInputMap(JComponent.WHEN_FOCUSED)
         .put(KeyStroke.getKeyStroke("ENTER"), "click");

        b.getActionMap().put("click", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                b.doClick();
            }
        });
    }
    
    private void syncSubtitle(long currentTime) {

        if (userSeeking) return;

        syncing = true;

        try {
            List<Subtitle> subtitles = model.getData();

            for (int i = 0; i < subtitles.size(); i++) {

                Subtitle s = subtitles.get(i);

                if (currentTime >= s.getStartTime()
                    && currentTime <= s.getEndTime()) {

                currentSubtitleRow = i;
                final int row = i;

                SwingUtilities.invokeLater(() -> {
                    if (table.getRowCount() > row) {

                        table.setRowSelectionInterval(row, row);

                        if (autoFollowSubtitle) {
                            table.scrollRectToVisible(table.getCellRect(row, 0, true));
                        }

                        table.repaint();
                    }
                });

                break;
            }
            }
        } finally {
            syncing = false;
        }
    }
    private void loadSubtitlesIntoVlc() {

        try {

            File srtFile = SrtWriter.writeTemp(model.getData());

            videoComponent.mediaPlayer()
                    .subpictures()
                    .setSubTitleFile(srtFile.getAbsolutePath());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void loadSubtitles(File file) {
        try {
            List<Subtitle> subtitles = SrtParser.parse(file);
            model.setData(subtitles);
            emptyPanel.setVisible(false);
            loadSubtitlesIntoVlc();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void disableSpace(AbstractButton b) {

        b.getInputMap(JComponent.WHEN_FOCUSED)
         .put(KeyStroke.getKeyStroke("SPACE"), "none");

        b.getInputMap(JComponent.WHEN_FOCUSED)
         .put(KeyStroke.getKeyStroke("released SPACE"), "none");
    }
    
    private String loadAscii(String path) {
        try (java.io.InputStream is = getClass().getResourceAsStream(path);
             java.util.Scanner sc = new java.util.Scanner(is)) {

            sc.useDelimiter("\\A");
            return sc.hasNext() ? sc.next() : "";

        } catch (Exception e) {
            e.printStackTrace();
            return "ASCII not found";
        }
    }
    
    private String formatTime(long millis) {

    long totalSeconds = millis / 1000;

    long hours = totalSeconds / 3600;
    long minutes = (totalSeconds % 3600) / 60;
    long seconds = totalSeconds % 60;

    return String.format(
            "%02d:%02d:%02d",
            hours,
            minutes,
            seconds
    );
}
    
}
