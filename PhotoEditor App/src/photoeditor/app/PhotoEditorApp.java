package photoeditor.app; 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;           

/**
 *
 * @author 1BestCsharp
 */
public class PhotoEditorApp extends JFrame {
    
    private BufferedImage originalImage;
    private BufferedImage editedImage;
    private final JLabel imageLabel;
    
    public PhotoEditorApp(){
        
        setTitle("Photo Editor App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        
        setLocationRelativeTo(null);
        
        createMenuBar();
        
        imageLabel = new JLabel();
        add(imageLabel, BorderLayout.CENTER);
        
        JButton grayScaleButton = new JButton("Gray Scale");
        grayScaleButton.setBackground(Color.darkGray);
        grayScaleButton.setForeground(Color.white);
        grayScaleButton.addActionListener(e -> applyGrayScaleFilter());
        
        JButton invertButton = new JButton("Invert");
        invertButton.setBackground(Color.darkGray);
        invertButton.setForeground(Color.white);
        invertButton.addActionListener(e -> applyInvertColorsFilter());
        
        JButton sepiaButton = new JButton("Sepia");
        sepiaButton.setBackground(Color.darkGray);
        sepiaButton.setForeground(Color.white);
        sepiaButton.addActionListener(e -> applySepiaFilter());
        
        JButton blurButton = new JButton("Blur");
        blurButton.setBackground(Color.darkGray);
        blurButton.setForeground(Color.white);
        blurButton.addActionListener(e -> applyBlurFilter());
        
        JButton edgeDetectionButton = new JButton("Edge Detection");
        edgeDetectionButton.setBackground(Color.darkGray);
        edgeDetectionButton.setForeground(Color.white);
        edgeDetectionButton.addActionListener(e -> applyEdgeDetectionFilter());
        
        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(Color.red);
        resetButton.setForeground(Color.white);
        resetButton.addActionListener(e -> resetImage());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(grayScaleButton);
        buttonPanel.add(invertButton);
        buttonPanel.add(sepiaButton);
        buttonPanel.add(blurButton);
        buttonPanel.add(edgeDetectionButton);
        buttonPanel.add(resetButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    
        private void createMenuBar(){
            // Create a menu bar for the application
            JMenuBar menuBar = new JMenuBar();
            // Create a "File" menu
            JMenu fileMenu = new JMenu("File");
            
            // Create "Open" menu item and set an action listener to handle its click event
            JMenuItem openItem = new JMenuItem("open");
            openItem.addActionListener(e -> openImage());
            
            // Create "Save" menu item and set an action listener to handle its click event
            JMenuItem saveItem = new JMenuItem("save");
            saveItem.addActionListener(e -> saveImage());
            
            // Add "Open" and "Save" menu items to the "File" menu
            fileMenu.add(openItem);
            fileMenu.add(saveItem);
            
            // Add the "File" menu to the menu bar
            menuBar.add(fileMenu);
                
            // Set the menu bar for the application window
            setJMenuBar(menuBar);
        }
        
        
        // Create a Method to update the image label with the edited image
        private void updateImageLabel(){
            
            // Create an ImageIcon from the editedImage
            ImageIcon imageIcon = new ImageIcon(editedImage);
            // Set the image icon on the imageLabel to display the edited image
            imageLabel.setIcon(imageIcon);
            // Revalidate the imageLabel to ensure the updated image is displayed correctly
            imageLabel.revalidate();
            
        }
        
        
        // Create a Method to reset the edited image back to the original image
        private void resetImage(){
            
            // Check if there is an original image available
            if(originalImage != null){
                // Create a deep copy of the original image and set it as the edited image
                editedImage = copyImage(originalImage);
                // Update the image label with the new edited image
                updateImageLabel();
                
            }
            
        }
        
        
        // create a method that creates a deep copy of a BufferedImage.
        private BufferedImage copyImage(BufferedImage image){
            
            // Get the ColorModel of the input image.
            ColorModel cm = image.getColorModel();
            // Check if the alpha channel of the image is premultiplied.
            boolean isAlphaPrm = cm.isAlphaPremultiplied();
            // Create a new WritableRaster that contains a copy of the image's data.
            WritableRaster raster = image.copyData(null);
            // Create and return a new BufferedImage using the ColorModel, WritableRaster,
            // and the information about the alpha channel pre-multiplication.
            return new BufferedImage(cm, raster, isAlphaPrm, null);
            
        }
        
    
        // create a method to select and display an image
        private void openImage(){
            // Create a file chooser dialog for selecting an image file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("images", "jpg", "png"));
            
            // Display the file chooser dialog and wait for the user to select a file
            int result = fileChooser.showOpenDialog(this);
            if(result == JFileChooser.APPROVE_OPTION)
            {
                 // Get the selected file
                File selectedFile = fileChooser.getSelectedFile();
                
                try
                {
                    // Check if the selected file is a valid image file
                    BufferedImage testImage = ImageIO.read(selectedFile);
                    if(testImage == null)
                    {
                        // Show an error message if the selected file is not a valid image
                        JOptionPane.showMessageDialog(this, "Invalid Image File Selected", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Store the original image and create a copy for editing
                    originalImage = testImage;
                    editedImage = copyImage(originalImage);
                    
                    // Update the image label to display the loaded image
                    updateImageLabel();
                }
                catch(Exception ex)
                {
                    // Print any exceptions that occur during image loading
                    ex.printStackTrace();
                    // Show an error message if there's an issue loading the image
                    JOptionPane.showMessageDialog(this, "Error Loading The Image", "Error", JOptionPane.ERROR_MESSAGE);                       
                }
            }                     
        }
        
        
        // create a Method to save the edited image
        private void saveImage()
        {

            // Create a file chooser dialog for selecting an image file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("images", "jpg", "png"));
            
            // Show the save dialog to allow the user to choose a location to save the image
            int result = fileChooser.showSaveDialog(this);
            // If the user selects a location and clicks the "Save" button
            if(result == JFileChooser.APPROVE_OPTION)
            {
                // Get the selected file and attempt to save the edited image to it
                File selectedFile = fileChooser.getSelectedFile();
                
                try
                {
                   
                    ImageIO.write(editedImage, "jpg", selectedFile);  
                }
                catch(Exception ex)
                {
                    // Print any exceptions that occur during image loading
                    ex.printStackTrace();
                    // Show an error message if there's an issue loading the image
                    JOptionPane.showMessageDialog(this, "Error Saving The Image", "Error", JOptionPane.ERROR_MESSAGE);                       
                }
            }  
            
        }
        
        
        
// create a method that applies a grayscale filter to the edited image using the luminosity method
    private void applyGrayScaleFilter()
    {
        // Check if the original image exists
        if(originalImage != null)
        {
            // Loop through all the pixels in the edited image
            for(int x = 0; x < editedImage.getWidth(); x++)
            {
                for(int y = 0; y < editedImage.getHeight(); y++)
                {
                  // Get the RGB value of the corresponding pixel in the original image
                  int rgb = originalImage.getRGB(x, y);
                  // Calculate the grayscale value using the luminosity method
                  int gray = (int) (0.3 * ((rgb >> 16) & 0xFF) + 0.59 * ((rgb >> 8) & 0xFF) + 0.11 * (rgb & 0xFF) );
                  
                  // Set the grayscale pixel in the edited image
                  editedImage.setRGB(x, y, (gray << 16) | (gray << 8) | gray);
                }
            }
            
            // Update the image label with the edited image
            updateImageLabel();
        }
    }
        
    
    // create a method that applies a sepia filter to the edited image
    private void applySepiaFilter()
    {
        // Check if the original image exists
        if(originalImage != null)
        {
            // Loop through all the pixels in the edited image
            for(int x = 0; x < editedImage.getWidth(); x++)
            {
                for(int y = 0; y < editedImage.getHeight(); y++)
                {
                    // Get the RGB value of the corresponding pixel in the original image
                    int rgb = editedImage.getRGB(x, y);
                    
                    // Extract the red, green, and blue components from the RGB value
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;
                    
                    // Calculate the sepia values for red, green, and blue components
                    int tr = (int) (0.393 * r + 0.769 * g + 0.189 * b);
                    int tg = (int) (0.349 * r + 0.686 * g + 0.168 * b);
                    int tb = (int) (0.272 * r + 0.534 * g + 0.131 * b);

                    // Ensure the sepia values are within the valid range of 0 to 255
                    tr = Math.min(255,tr);
                    tg = Math.min(255,tg);
                    tb = Math.min(255,tb);
                    
                    // Set the sepia pixel in the edited image
                    editedImage.setRGB(x, y, (tr << 16) | (tg << 8) | tb);
                }
            }
            
            // Update the image label with the edited image
            updateImageLabel();
        }
    }
        
    
// create a that method applies a simple blur filter to the edited image using a 3x3 kernel.
private void applyBlurFilter()
{
    
    // Check if the original image exists
        if(originalImage != null)
        {
            // Define a 3x3 kernel for blurring
            int[][] kernel = {{1,1,1},{1,1,1},{1,1,1}};
            int kernelSum = 9; // Sum of kernel values (used for normalization)
            
            // Loop through all the pixels in the edited image (excluding the borders)
            for(int x = 1; x < editedImage.getWidth() - 1; x++)
            {
                for(int y = 1; y < editedImage.getHeight() - 1; y++)
                {
                    // Initialize RGB values to 0 for each pixel
                    int r = 0, g = 0, b = 0;
                    
                    // Convolve the kernel over the neighboring pixels
                    for(int i = -1; i <= 1; i++)
                    {
                        for(int j = -1; j <= 1; j++)
                        {
                          // Get RGB value of neighboring pixel  
                           int rgb = originalImage.getRGB(x+i, y+i);
                  // Multiply each color component by the corresponding kernel value and accumulate
                           r += ((rgb >> 16) & 0xFF) * kernel[i+1][j+1];
                           g += ((rgb >> 8) & 0xFF) * kernel[i+1][j+1];;
                           b += (rgb & 0xFF) * kernel[i+1][j+1];;
                        }    
                    }
                    
                    // Normalize the accumulated values based on the kernel sum
                    r /= kernelSum; 
                    g /= kernelSum; 
                    b /= kernelSum;
                    
                    // Compose the new RGB value using the blurred color components
                    int newRgb = (r << 16) | (g << 8) | b;
                    // Update the edited image with the new RGB value
                    editedImage.setRGB(x, y, newRgb);
                }
            }
            
            // Update the label displaying the edited image to reflect the changes
            updateImageLabel();
            
        }
    
}
   
    
// create a method that applies the invert colors filter to the editedImage.
private void applyInvertColorsFilter()
{
           // Check if the original image exists
        if(originalImage != null)
        {
            // Loop through all the pixels in the edited image
            for(int x = 0; x < editedImage.getWidth(); x++)
            {
                for(int y = 0; y < editedImage.getHeight(); y++)
                {
                    // Get the RGB color value at the current pixel in the originalImage.
                    int rgb = originalImage.getRGB(x, y);
                    // Extract the red component, invert it, and store it in r.
                    int r = 255 - ((rgb >> 16) & 0xFF);
                    // Extract the green component, invert it, and store it in g.
                    int g = 255 - ((rgb >> 8) & 0xFF);
                    // Extract the blue component, invert it, and store it in b.
                    int b = 255 - (rgb & 0xFF);
                    // Combine the inverted RGB components and set the pixel value in the editedImage.
                    editedImage.setRGB(x, y, (r << 16) | (g << 8) | b);
                }  
            }
            
            // Update the image label to reflect the changes made to the editedImage.
            updateImageLabel();
        }
}
    
    


// create a method that applies the edge detection filter
private void applyEdgeDetectionFilter()
{

        // Check if the original image exists
        if(originalImage != null)
        {
            // Define the Sobel operators for x and y directions
            int[][] sobelx = {{-1,0,1},{-2,0,2},{-1,0,1}};
            int[][] sobely = {{1,2,1},{0,0,0},{-1,-2,-1}};
            
            // Create a new BufferedImage to store the grayscale version of the originalImage
            BufferedImage grayImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            Graphics g = grayImage.getGraphics();
            g.drawImage(originalImage, 0, 0, null);
            g.dispose();
            
        // Loop through each pixel of the editedImage (excluding the borders)
            for(int x = 1; x < editedImage.getWidth() - 1; x++)
            {
                for(int y = 1; y < editedImage.getHeight() - 1; y++)
                {
                    // Initialize gx and gy to store the gradient in the x and y directions, respectively
                    int gx = 0, gy = 0;
                    
                    // Apply the Sobel operator to the surrounding pixels of the grayscale image
                    for(int i = -1; i <= 1; i++)
                    {
                        for(int j = -1; j <= 1; j++)
                        {
                          // Get the grayscale value of the pixel
                          int gray = grayImage.getRGB(x + i, y + j) & 0xFF;
                          
                          // Apply the Sobel operator and accumulate the results in gx and gy
                          gx += gray * sobelx[i+1][j + 1];
                          gy += gray * sobely[i+1][j + 1];
                        }
                    }
                    
                    // Calculate the magnitude of the gradient using the Euclidean distance formula
                    int magnitude = (int) Math.sqrt(gx * gx + gy * gy);
                    // Create a new RGB value with the same magnitude for each color component
                    int newRgb = (magnitude << 16) | (magnitude << 8) | magnitude;
                   
                    // Set the newRGB value for the corresponding pixel in the editedImage
                    editedImage.setRGB(x, y, newRgb);
                    
                }
            }
            
            // Update the label displaying the editedImage
            updateImageLabel();
        }
}

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // use the flatlaf lib to make the app look flat
        try{
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        
        new PhotoEditorApp();
        
    }

}
