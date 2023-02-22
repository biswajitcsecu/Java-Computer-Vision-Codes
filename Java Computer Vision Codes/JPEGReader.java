import vtk.vtkNativeLibrary;
import vtk.vtkRenderWindowInteractor;
import vtk.vtkJPEGReader;
import vtk.vtkImageViewer2;

public class JPEGReader
{
  // --------------- Load VTK library------------- 
  static{vtkNativeLibrary.LoadAllNativeLibraries();}

  public static void main(String args[]){

    String inputFilename = "/home/picox/Projects/Eclipse/JPEGReader/src/2.jpg";

    //----------Read the image----------------
    vtkJPEGReader jpegReader = new vtkJPEGReader();
    jpegReader.SetFileName (inputFilename);
    
    jpegReader.SetDataExtent(0, 63, 0, 63, 1, 93);
    jpegReader.SetDataSpacing(3.2, 3.2, 1.5);
    jpegReader.SetDataOrigin(0.0, 0.0, 0.0);
    jpegReader.SetDataScalarTypeToUnsignedShort();
    jpegReader.SetDataByteOrderToLittleEndian();
    jpegReader.UpdateWholeExtent();

    //----------- Visualize------------
    vtkImageViewer2 imageViewer = new vtkImageViewer2();
    imageViewer.SetInputConnection(jpegReader.GetOutputPort() );
    vtkRenderWindowInteractor renderWindowInteractor = new vtkRenderWindowInteractor();
    imageViewer.SetupInteractor(renderWindowInteractor);
    imageViewer.Render();
    imageViewer.GetRenderer().ResetCamera();
    imageViewer.Render();

    renderWindowInteractor.Start();
  }
}
