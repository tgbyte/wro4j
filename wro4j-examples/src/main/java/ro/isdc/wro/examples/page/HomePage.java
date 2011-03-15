package ro.isdc.wro.examples.page;


import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;


/**
 * Homepage
 */
public class HomePage extends AbstractBasePage {
  private static final long serialVersionUID = 1L;
  /**
   * Constructor that is invoked when page is invoked without a session.
   *
   * @param parameters Page parameters
   */
  public HomePage(final PageParameters parameters) {
    super(parameters);
    add(new BookmarkablePageLink<Void>("processors", ProcessorsPage.class));
  }
}
