package net.clearcontrol.easyscopy.lightsheet;

import clearcl.ClearCLContext;
import clearcontrol.devices.lasers.LaserDeviceInterface;
import clearcontrol.microscope.lightsheet.LightSheetMicroscope;
import clearcontrol.microscope.lightsheet.imaging.DirectImage;
import clearcontrol.microscope.lightsheet.imaging.DirectImageStack;
import clearcontrol.microscope.lightsheet.processor.LightSheetFastFusionEngine;
import clearcontrol.microscope.lightsheet.processor.LightSheetFastFusionProcessor;
import net.clearcontrol.easyscopy.EasyMicroscope;
import net.clearcontrol.easyscopy.EasyScope;
import org.atteo.classindex.ClassIndex;

import java.util.ArrayList;

/**
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * February 2018
 */
public abstract class EasyLightsheetMicroscope extends EasyMicroscope
{
  private LightSheetMicroscope mLightSheetMicroscope;

  public EasyLightsheetMicroscope(LightSheetMicroscope lLightSheetMicroscope) {
    super(lLightSheetMicroscope);
    mLightSheetMicroscope = lLightSheetMicroscope;
  }


  // -----------------------------------------------------------------
  // imaging
  public DirectImage getDirectImage() {
    DirectImage lDirectImage = new DirectImage(mLightSheetMicroscope);
    return lDirectImage;
  }

  public DirectImage getDirectImage(int pLightSheetZ, int pDetectionArmZ) {
    DirectImage lDirectImage = getDirectImage();
    lDirectImage.setIlluminationZ(pLightSheetZ);
    lDirectImage.setDetectionZ(pDetectionArmZ);
    return lDirectImage;
  }

  public DirectImageStack getDirectImageStack() {
    return new DirectImageStack(mLightSheetMicroscope);
  }

  // -----------------------------------------------------------------
  // general
  @Override
  public void terminate() {
    mLightSheetMicroscope.stop();
    mLightSheetMicroscope.close();
  }

  public LightSheetMicroscope getLightSheetMicroscope()
  {
    return mLightSheetMicroscope;
  }


}
