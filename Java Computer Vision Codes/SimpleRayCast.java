import vtk.vtkNamedColors;
import vtk.vtkNativeLibrary;
import vtk.vtkRenderWindow;
import vtk.vtkRenderWindowInteractor;
import vtk.vtkRenderer;
import vtk.vtkStructuredPointsReader;
import vtk.vtkPiecewiseFunction;
import vtk.vtkActor;
import vtk.vtkColorTransferFunction;
import vtk.vtkVolumeProperty;
import vtk.vtkFixedPointVolumeRayCastMapper;
import vtk.vtkVolume;


public class SimpleRayCast 
{
  // Load VTK library
  static{vtkNativeLibrary.LoadAllNativeLibraries();}

  public static void main(String args[]) 
  {

    String inputFilename = "/home/picox/Projects/Eclipse/SimpleRayCast/src/ironProt.vtk";

    vtkNamedColors colors = new vtkNamedColors();

    //For Actor Color
    double actorColor[] = new double[4];
    //Renderer Background Color
    double Bgcolor[] = new double[4];

    colors.GetColor("Tomato", actorColor);
    colors.GetColor("Lemon", Bgcolor);

    // Create the reader for the data
    vtkStructuredPointsReader reader = new vtkStructuredPointsReader();
    reader.SetFileName(inputFilename);

    // Create the renderer, render window and interactor.
    vtkRenderer ren = new vtkRenderer();
    vtkRenderWindow renWin = new vtkRenderWindow();
    renWin.AddRenderer(ren);
    vtkRenderWindowInteractor iren = new vtkRenderWindowInteractor();
    iren.SetRenderWindow(renWin);

    // Create transfer mapping scalar value to opacity
    vtkPiecewiseFunction opacityTransferFunction = new vtkPiecewiseFunction();
    opacityTransferFunction.AddPoint(20, 0.0);
    opacityTransferFunction.AddPoint(255, 0.2);

    // Create transfer mapping scalar value to color
    vtkColorTransferFunction colorTransferFunction = new vtkColorTransferFunction();
    colorTransferFunction.AddRGBPoint(0.0, 0.0, 0.0, 0.0);
    colorTransferFunction.AddRGBPoint(64.0, 1.0, 0.0, 0.0);
    colorTransferFunction.AddRGBPoint(128.0, 0.0, 0.0, 1.0);
    colorTransferFunction.AddRGBPoint(192.0, 0.0, 1.0, 0.0);
    colorTransferFunction.AddRGBPoint(255.0, 0.0, 0.2, 0.0);

    // The property describes
    vtkVolumeProperty volumeProperty = new vtkVolumeProperty();
    volumeProperty.SetColor(colorTransferFunction);
    volumeProperty.SetScalarOpacity(opacityTransferFunction);
    volumeProperty.ShadeOn();
    volumeProperty.SetInterpolationTypeToLinear();

    // The mapper to render the data
    vtkFixedPointVolumeRayCastMapper volumeMapper = new vtkFixedPointVolumeRayCastMapper();
    volumeMapper.SetInputConnection(reader.GetOutputPort());

    // The volume used to position/orient
    vtkVolume volume = new vtkVolume();
    volume.SetMapper(volumeMapper);
    volume.SetProperty(volumeProperty);

    ren.AddVolume(volume);
    ren.SetBackground(Bgcolor);
    ren.GetActiveCamera().Azimuth(45);
    ren.GetActiveCamera().Elevation(30);
    ren.ResetCameraClippingRange();
    ren.GetActiveCamera().Azimuth(0);
    ren.GetActiveCamera().SetClippingRange(0.1, 1000.0);
           
    //Auto rotation
    vtkActor actor = new vtkActor();
    actor.SetOrientation(0, 0, 0);
    actor.RotateX(60);
    renWin.Render();
    renWin.Render();
    renWin.EraseOff();
    ren.ResetCamera();
    renWin.SetSize(1024, 840);
    renWin.EraseOn();    
    renWin.Render();
    iren.Initialize();
    iren.Start();
  }
}
