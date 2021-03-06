/**
 * Copyright wro4j@2011
 */
package ro.isdc.wro.model.resource.processor.decorator;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import ro.isdc.wro.model.group.processor.Minimize;
import ro.isdc.wro.model.resource.ResourceType;
import ro.isdc.wro.model.resource.SupportedResourceType;
import ro.isdc.wro.model.resource.processor.MinimizeAware;
import ro.isdc.wro.model.resource.processor.ResourcePostProcessor;
import ro.isdc.wro.model.resource.processor.ResourcePreProcessor;
import ro.isdc.wro.model.resource.processor.SupportedResourceTypeAware;
import ro.isdc.wro.util.AbstractDecorator;
import ro.isdc.wro.util.ObjectDecorator;


/**
 * Hides details common to all processors decorators, like ability to identify if a processor is minimize aware
 *
 * @author Alex Objelean
 * @created 11 Apr 2012
 * @since 1.4.6
 */
public abstract class AbstractProcessorDecoratorSupport
  implements ResourcePreProcessor, ResourcePostProcessor, SupportedResourceTypeAware, MinimizeAware, ObjectDecorator<Object> {

  /**
   * This method is final, because it intends to preserve the getSupportedResourceType flag of the decorated processor. You still can
   * override this behavior by implementing {@link AbstractProcessorDecoratorSupport#getSupportedResourceTypeInternal()} on your own
   * risk.
   */
  public final SupportedResourceType getSupportedResourceType() {
    return getSupportedResourceTypeInternal();
  }

  /**
   * Allow subclass override the way getSupportedResourceType is used. 
   */
  protected SupportedResourceType getSupportedResourceTypeInternal() {
    return getSupportedResourceTypeForProcessor(getDecoratedObject());
  }

  /**
   * Computes {@link SupportedResourceType} for provided processor.
   */
  final SupportedResourceType getSupportedResourceTypeForProcessor(final Object processor) {
    SupportedResourceType supportedType = processor.getClass().getAnnotation(SupportedResourceType.class);
    /**
     * This is a special case for processors which implement {@link SupportedResourceTypeProvider} interface. This is
     * useful for decorator processors which needs to "inherit" the {@link SupportedResourceType} of the decorated
     * processor.
     */
    if (processor instanceof SupportedResourceTypeAware) {
      supportedType = ((SupportedResourceTypeAware) processor).getSupportedResourceType();
    }
    return supportedType;
  }
  
  /**
   * This method is final, because it intends to preserve the minimize flag of the decorated processor. You still can
   * override this behavior by implementing {@link AbstractProcessorDecoratorSupport#isMinimizeInternal()} on your own
   * risk.
   */
  public final boolean isMinimize() {
    return isMinimizeInternal();
  }

  /**
   * Allow subclass override the way isMinimized is used. 
   */
  protected boolean isMinimizeInternal() {
    return isMinimizeForProcessor(getDecoratedObject());
  }

  /**
   * @return true if passed processor is minimize aware.
   */
  final boolean isMinimizeForProcessor(final Object processor) {
    if (processor instanceof MinimizeAware) {
      return ((MinimizeAware) processor).isMinimize();
    }
    return processor.getClass().isAnnotationPresent(Minimize.class);
  }
  
  /**
   * @return the array of supported resources the processor can process.
   */
  public final ResourceType[] getSupportedResourceTypes() {
    final SupportedResourceType supportedType = getSupportedResourceType();
    return supportedType == null ? ResourceType.values() : new ResourceType[] {
      supportedType.value()
    };
  }
  
  /**
   * @return the decorated processor. The type of the returned object is {@link Object} because we don't really care and
   *         we need it only to check if the processor is minimize aware and get its supported type. This "hack" will e
   *         removed in 1.5.0.
   */
  public abstract Object getDecoratedObject();
  
  /**
   * {@inheritDoc}
   */
  public Object getOriginalDecoratedObject() {
    return AbstractDecorator.getOriginalDecoratedObject(getDecoratedObject());
  }

  /**
   * {@inheritDoc}
   */
  public final void process(final Reader reader, final Writer writer)
    throws IOException {
    process(null, reader, writer);
  }
}
