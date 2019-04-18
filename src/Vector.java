import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;


public class Vector extends javax.swing.JFrame {

	public static int [][] originalImage ;
	public static int widthOfBlock;
	public static int heightOfBlock;
	public static int OrgImgrows;
	public static int OrgImgcols;
    static class vector 
    {  
        int width ;
        int height ;
        double [][] data ;
        public vector () {}
        public vector(int width, int height) {
            this.width = width;
            this.height = height;
            this.data = new double [height][width];
        }
    }
    
    static class split_element 
    {
        vector value ;
        ArrayList<vector> assoicated = new ArrayList<>();
        
        public split_element() {}
        
        public split_element(vector value ,ArrayList<vector> assoicated ) {
            this.value = value;this.assoicated = assoicated ;
        }
        public vector getValue() {
            return value;
        }
        public void setValue(vector value) {
            this.value = value;
        }
        public ArrayList<vector> getAssoicated() {
            return assoicated;
        }
    }
	

	private static final long serialVersionUID = 1L;


     public static int[][] readImage(String filePath)
    {
	    int width=0;
		int height=0;
        File file=new File(filePath);
        BufferedImage image=null;
        try
        {
            image=ImageIO.read(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

          width=image.getWidth();
          height=image.getHeight();
        int[][] pixels=new int[height][width];

        for(int x=0;x<width;x++)
        {
            for(int y=0;y<height;y++)
            {
                int rgb=image.getRGB(x, y);
                int r = (rgb >> 16) & 0xff;
                pixels[y][x]=r;
            }
        }

        return pixels;
    }
    
    public static void writeImage(int[][] pixels,String outputFilePath,int width,int height)
    {
        File fileout=new File(outputFilePath);
        BufferedImage image2=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB );

        for(int x=0;x<width ;x++)
        {
            for(int y=0;y<height;y++)
            {
                image2.setRGB(x,y,(pixels[y][x]<<16)|(pixels[y][x]<<8)|(pixels[y][x]));
            }
        }
        try
        {
            ImageIO.write(image2, "jpg", fileout);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    

    static int indxOF_min_distance (ArrayList <Double> distance_difference )
     {
         double min_diff = distance_difference.get(0); 
         int indx = 0 ;
         
         for (int i=1 ; i<distance_difference.size() ; i++)
         {
             if ( distance_difference.get(i) < min_diff)
             {
                min_diff = distance_difference.get(i);
                indx = i ;
             }
             
         }
         
         return indx ;
     }
    static void ShowVector ( vector v)
    {
        for (int i=0 ; i<v.height ; i++ )
        {
            for (int j=0 ; j<v.width ; j++)
            {
                System.out.print(v.data[i][j] + "  ");
            }
            System.out.println();
        }
        
        System.out.println("---------------------------");
    }
    static Scanner sc;
    static Formatter out; //34an yktb el tag be format el string
    
    public static void open_file(String FileName) {
        try {
            sc = new Scanner(new File(FileName));
        } catch (Exception e) {

        }
    }
    public static void openfile(String pass) {
        try {
            out = new Formatter(pass);
        } catch (Exception e) {
        }

    }
    static void writeOnFile(String code) {
        out.format("%s", code);
        out.format("%n");
        out.flush(); 

    }
    
    
    
    static ArrayList <vector> DivideOrgImgToVectors  (vector [][] vectors )
    {   
        
        ArrayList <vector> AllVectors = new ArrayList<>();
        vector obj ;
        
        for (int i=0 ; i<originalImage.length ; i+=heightOfBlock)
        {
            for (int j=0 ; j<originalImage[0].length ; j+=widthOfBlock)
            {   
               int x = i ;
               int z = j ;
               obj = new vector ( widthOfBlock , heightOfBlock );
               
               for (int n=0 ; n<heightOfBlock ; n++)
                {
                    for (int m=0 ; m<widthOfBlock ; m++)
                    {    
                    	obj.data[n][m]= originalImage[x][z++];
                    }
                    
                    x++;
                    z=j;
                }  
                
                AllVectors.add(obj);
            }
        }
        
        int indx =0 ;
        
        for (int i=0 ; i<OrgImgrows ; i++) // filling the new matrix that consists of vectors onli 
        {
            for (int j=0 ; j<OrgImgcols ; j++)
            {
                vectors[i][j] = AllVectors.get(indx++);
            }
        }
        
        return AllVectors ;
   }
    
    
     static ArrayList<vector> associate ( ArrayList<vector> split , ArrayList <vector> data  ) // associate ang return avg
    {   
        ArrayList <split_element> Split = new ArrayList<>();
        
        int width = data.get(0).width;
        int height = data.get(0).height ;
                
        for (int i = 0; i < split.size(); i++)  // inilialization 
        {  
           split_element initial = new split_element() ;
           initial.setValue(split.get(i));
           Split.add(initial);				/** hmla el arraylist behom **/
        }
        
        for (int i=0 ; i<data.size() ; i++) // associate data
        {
                  vector cur = data.get(i);		//hmsk kwl vector mn eldata w a7wlw l diffenrece w a4of men 22rb vector leh
                  ArrayList <Double> distance_difference = new ArrayList<> ();
                  for (int j=0 ; j<split.size() ;j++)
                  {   
                      double total_diff = 0 ;
                      
                      for (int w=0 ; w<width ; w++)
                      {
                          for (int h=0 ; h<height ; h++)
                          {
                              double value = cur.data[w][h]-split.get(j).data[w][h];
                              double distanc_diff =  Math.pow( value , 2);
                              total_diff +=distanc_diff ;
                          }
                      }
                    distance_difference.add(total_diff);
                 }
                  
                  int indx = indxOF_min_distance (distance_difference);			/** nfs el klam hena h4of men be3ml mn elcodebook 22l diffrenece  **/ 
                  ArrayList <vector> cur_associated = Split.get(indx).getAssoicated(); /** h5ly el vector elly m3ya yblong llvector elly gably 22l difference da **/
                  
                  cur_associated.add(cur);
                 
                  split_element New = new split_element(Split.get(indx).getValue() , cur_associated);
                  
                  Split.set(indx , New );  //h3ml 2pdate b2a llassociated vectors bta3t elly l2aet 3ndo min diffrence da
          } 
       
        // b3d ma 3mlt el assotiated vectors h2padte el averages bta3thom 
        ArrayList <vector> Averages = new ArrayList<> ();
        for (int i=0 ; i<Split.size() ; i++) // calculate average for the associated values
        {
            int arraysize = Split.get(i).getAssoicated().size();
            vector avg = new vector(width , height);
            
            for (int w = 0; w < width; w++) 
            {
                for (int h = 0; h < height; h++) 
                {   
                    double total = 0 ;
                   
                    for (int j = 0; j < arraysize; j++) 
                    {   
                        total+= Split.get(i).getAssoicated().get(j).data[w][h];
                    }
                    
                    avg.data[w][h]= total/arraysize;		//avg=sum/size
                } 
               
            }
            Averages.add(avg);
            
        }
        
        return Averages ;
    }
    
    static ArrayList<vector> Split (  ArrayList <vector> data , int numoflevels ) 
    {
    	
       ArrayList <vector> Averages = new ArrayList<>();         
       vector first_avg = new vector( widthOfBlock , heightOfBlock );
       for (int w = 0; w < widthOfBlock; w++) 
       {  
           for (int h = 0; h < heightOfBlock; h++) 
           {   
                double total = 0 ; 
               for (int i = 0; i < data.size(); i++) 
               {
                       total += data.get(i).data[w][h];
               }
               first_avg.data[w][h] = total/data.size();

          }
       }
         Averages.add(first_avg);
         int width = Averages.get(0).width ;
         int height = Averages.get(0).height ;
         while(true) {
            if (Averages.size()<numoflevels)
            {
            	ArrayList <vector> split = new ArrayList<>();
            	for (int j=0 ; j<Averages.size() ; j++)
            	{   
            		vector left = new vector( width , height);
            		vector right = new vector( width , height);
            		for (int w=0 ; w<width ; w++)
            		{
            			for (int h=0 ; h<height ; h++)
            			{   
            				int lo = (int)Averages.get(j).data[w][h] ;
            				left.data[w][h]= lo;
            				right.data[w][h]= lo+1;
                   
            			}
              
            		}
              
            		split.add(left);
            		split.add(right);  
            	}
            	Averages.clear();
            	Averages = associate( split , data);
            
            }
            else 
                break;
         }
         return Averages ;
    }
    
     
    static void Quantization ( int numoflevels ,  ArrayList <vector> data ,vector [][] vectors )
    {    
         ArrayList <vector> Averages = new ArrayList<>();         
        Averages = Split (data , numoflevels );
        System.out.println("Done split "+"\n"+"Finaaaal Avergaes");
        for (int i=0 ; i<Averages.size(); i++)
        {
            ShowVector(Averages.get(i));
        }
        ArrayList <vector> codeBook = new ArrayList<>();
        for (int i=0 ; i<Averages.size() ; i++)
        {
            codeBook.add(Averages.get(i));
        }
        compress (codeBook , vectors );
    } 
    
   static void compress ( ArrayList<vector> codeBook , vector [][] vectors )
    {
       int Rows = vectors.length ;
       int Cols = vectors[0].length ;
       int [][] comp_image = new int [Rows][Cols];
       
       for (int i=0 ; i<Rows ; i++)
       {
           for (int j=0 ; j<Cols ; j++)
           {
                vector cur = vectors[i][j];
                ArrayList <Double> distance_difference = new ArrayList<> ();
                
                for (int k=0 ; k<codeBook.size() ;k++)
                {   
                    double total_diff = 0 ;
                  
                    for (int w=0 ; w<codeBook.get(0).width ; w++)
                    {
                        for (int h = 0; h < codeBook.get(0).height; h++)		// hmsk kwl block whlf 3 el codebook kolo we 24of a2l vector by3ml diffenrence m3 el block da
                        {
                            double value = cur.data[w][h] - codeBook.get(k).data[w][h];
                            double distanc_diff = Math.pow(value, 2);
                            total_diff += distanc_diff;
                        }
                    }
                    distance_difference.add(total_diff);
                }
                int indx = indxOF_min_distance (distance_difference);
                comp_image[i][j]= indx ;
               
           }
       }
        Save_CodeBook_CompImg ( codeBook , comp_image);
       
    }
    
    
    
    static void Decompress ()
    {
        
        ArrayList<vector> codeBook = new ArrayList <vector>();
        open_file("CompressFile.txt");
        int codeBookSize = Integer.parseInt(sc.nextLine());		// start by reading the codebook first
        int WidthOfBlock = Integer.parseInt(sc.nextLine());
        int heightOfBlock = Integer.parseInt(sc.nextLine());
        
        for (int i=0 ; i<codeBookSize ; i++)
        {
            vector cur = new vector(WidthOfBlock , heightOfBlock);
             
            for (int w=0 ; w<WidthOfBlock ; w++)
            {  
                String row = sc.nextLine();
                String [] elements = row.split(" ");
                
                for (int h=0 ; h<heightOfBlock ; h++)
                {
                  cur.data[w][h]= Double.parseDouble(elements[h]);
                }
                
            }
            
            codeBook.add(cur);
           
        }  
        // done reading codeBook 
        int com_image_height = Integer.parseInt(sc.nextLine());					// take the diementions of the compressed photo from the file
        int com_image_width =  Integer.parseInt(sc.nextLine());
        int [][]  comp_image = new int [com_image_height][com_image_width];
        
        for (int i=0 ; i<comp_image.length ; i++)							// read the image matrix
        {   
            String line = sc.nextLine();
            String [] row = line.split(" ");
            
            for (int j=0 ; j<comp_image[0].length ; j++)
            {
                comp_image[i][j] = Integer.parseInt(row[j]);
            }
            
        }
        sc.close();
       
        int [][] Decomp_image = new int [originalImage.length][originalImage[0].length];  // from the compressed matrix and the codebook create the decompressed matrix
        for (int i=0 ; i<comp_image.length ; i++)
        {
            for (int j=0 ; j<comp_image[0].length ; j++)
            {
                vector cur = new vector();
                cur = codeBook.get(comp_image[i][j]);
                
                int cornerx = i*cur.height;
                int cornery = j*cur.width ;
                
                
                for (int h=0 ; h<cur.height ; h++)			//hmla el matrix block block
                {
                    
                    for (int k=0 ; k<cur.width ; k++)
                    {
                        Decomp_image[cornerx+h][cornery+k] = (int) cur.data[h][k];
                    }
                }
                
            }
        }
        
        
        System.out.println("MissionCompleted :D Compressed Image:"+"\n"+Decomp_image);
        writeImage(Decomp_image, "Decompress.jpg", Decomp_image[0].length, Decomp_image.length);
        
            
        
    }
    
    static void Save_CodeBook_CompImg ( ArrayList<vector> codeBook , int [][] comp_image )
    {
        openfile("CompressFile.txt");
        String codeBookSize = "" + codeBook.size();	
        String WidthOfBlock = "" + codeBook.get(0).width;
        String heightOfBlock = "" + codeBook.get(0).height;
        writeOnFile(codeBookSize);							// first save the codebook data, width and height of the block and it's size 
        writeOnFile(WidthOfBlock);
        writeOnFile(heightOfBlock);
        
        for (int i=0 ; i<codeBook.size() ; i++)
        {
            for (int w=0 ; w<codeBook.get(i).width ; w++)
            { 
                String row = "";
                for (int h=0 ; h<codeBook.get(i).height ; h++)
                {
                    row += codeBook.get(i).data[w][h] + " ";
                }
                writeOnFile(row);
            }
            
        }
        												// after storing the codebook store the compressed image
        String com_image_height = "" + comp_image.length ;
        writeOnFile(com_image_height);
        String com_image_width = "" + comp_image[0].length ;
        writeOnFile(com_image_width);					// it's dimensions first the the image it self
        
        for (int i=0 ; i<comp_image.length ; i++)
        {   
            String row = "";
            for (int j=0 ; j<comp_image[0].length ; j++)
            {
                row+= comp_image[i][j] +" ";
            }
            writeOnFile(row);
        }
        out.close();
    }
    public static void main(String []args)
    {
    	Scanner sc=new Scanner(System.in);

        int numOfLevels = sc.nextInt();
        widthOfBlock =sc.nextInt();
        heightOfBlock = sc.nextInt();
        originalImage  = readImage("PB.jpg");
        OrgImgrows = originalImage.length /heightOfBlock ; // lel new matrix li mtkwna mn vectors 
        OrgImgcols = originalImage[0].length /heightOfBlock ; 
        vector [][] vectors = new vector [OrgImgrows][OrgImgcols]; // 2D array consist of vectors 
        ArrayList <vector> data = DivideOrgImgToVectors (vectors);
        int indx =0 ;
        for (int i=0 ; i<widthOfBlock ; i++) // filling the new matrix that consists of vectors onli 
        {
            for (int j=0 ; j<OrgImgcols ; j++)
            {
                vectors[i][j] = data.get(indx++);
            }
        }
        Quantization (numOfLevels , data ,vectors  );
       sc.close();

       Decompress ();
    	
    	
    }
}
