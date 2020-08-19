import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.commons.lang3.StringEscapeUtils

import org.bonitasoft.web.extension.page.PageContext;
import org.bonitasoft.web.extension.page.PageController;
import org.bonitasoft.web.extension.page.PageResourceProvider;

import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.CommandAPI;
import org.bonitasoft.engine.command.CommandDescriptor;
import org.bonitasoft.engine.command.CommandCriterion;
import org.bonitasoft.engine.identity.UserUpdater;
import org.bonitasoft.engine.exception.UpdateException;

import org.codehaus.groovy.tools.shell.CommandAlias;

import java.util.logging.Logger;
import org.json.simple.JSONObject;

public class Index implements PageController {

	private static String pageName="changepassword";
	private static Logger logger= Logger.getLogger("org.bonitasoft.custompage."+pageName+".groovy");

		@Override
		public void doGet(HttpServletRequest request, HttpServletResponse response, PageResourceProvider pageResourceProvider, PageContext pageContext) {

				Logger logger= Logger.getLogger("org.bonitasoft");


				String indexContent;
				pageResourceProvider.getResourceAsStream("Index.groovy").withStream { InputStream s-> indexContent = s.getText() };
				response.setCharacterEncoding("UTF-8");
				PrintWriter out = response.getWriter()

				String action=request.getParameter("action");
				logger.info("CustomPage:action ["+action+"] !");
				if (action==null || action.length()==0 ) {
						// logger.severe("###################################### RUN Default !");

						runTheBonitaIndexDoGet( request, response,pageResourceProvider,pageContext);
						return;
				}

				APISession ApiSession = pageContext.getApiSession()
				IdentityAPI identityApi = TenantAPIAccessor.getIdentityAPI(ApiSession);
				String statusChange="badAction";

				if ("ping".equals(action)) {
					statusChange="ping userId["+ApiSession.getUserId()+"]";
				}
				
				else if ("changepassword".equals(action)) {
						
						String passwordUri=request.getParameter("password");
						String password = (passwordUri==null ? null : java.net.URLDecoder.decode(passwordUri, "UTF-8"));
						// logger.info("CustomPage:action ChangePassword to ["+password+"] !");
				
						try{
								UserUpdater updateDescriptor = new UserUpdater();
								updateDescriptor.setPassword(password);
								identityApi.updateUser(ApiSession.getUserId(), updateDescriptor);
								
								statusChange="Your password changed";
						} catch ( UpdateException e){
								StringWriter sw = new StringWriter();
								e.printStackTrace(new PrintWriter(sw));
								String exceptionDetails = sw.toString();
								statusChange="Error during change : "+exceptionDetails;
						}


						
				}
				
				out.write( statusChange );
					out.flush();
						out.close();
						return;
		}

		
		
		/**
		 * 
		 * @param request
		 * @param response
		 * @param pageResourceProvider
		 * @param pageContext
		 */
		private void runTheBonitaIndexDoGet(HttpServletRequest request, HttpServletResponse response, PageResourceProvider pageResourceProvider, PageContext pageContext) {
				try {
						String indexContent;
						pageResourceProvider.getResourceAsStream("index.html").withStream { InputStream s->
								indexContent = s.getText()
						}

						// String pageResource="pageResource?&page="+ request.getParameter("page")+"&location=";
						// 7.0 Living update does not need that
						// indexContent= indexContent.replace("@_USER_LOCALE_@", request.getParameter("locale"));
						// indexContent= indexContent.replace("@_PAGE_RESOURCE_@", pageResource);

						indexContent= indexContent.replace("@_CURRENTTIMEMILIS_@", String.valueOf(System.currentTimeMillis()));

						
						response.setCharacterEncoding("UTF-8");
						PrintWriter out = response.getWriter();
						out.print(indexContent);
						out.flush();
						out.close();
				} catch (Exception e) {
					logger.severe("CustomPageChangepassword:Exception  "+e.getMessage());

				}
		}
}
