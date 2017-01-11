package rs.make.alfresco.actions.container.mimetypes;

import java.util.List;
import java.util.regex.Pattern;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.ContentData;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.tagging.TaggingService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;

public class MakeContainerMimeTypesSet extends ActionExecuterAbstractBase {

	private NodeService nodeService;
	public NodeService getNodeService() {
		return nodeService;
	}
	public void setNodeService( NodeService nodeService ) {
		this.nodeService = nodeService;
	}

	private TaggingService taggingService;
	public TaggingService getTaggingService() {
		return taggingService;
	}
	public void setTaggingService( TaggingService taggingService ) {
		this.taggingService = taggingService;
	}

	private static Logger logger = Logger.getLogger( MakeContainerMimeTypesSet.class );

	public static final String NAME = "MakeContainerMimeTypesSet";
	public static final QName PARAM_TYPE = ContentModel.TYPE_CONTENT;
	public static final String TAG_PREFIX = "mimetype-";
	public static final String TAG_GROUP_PREFIX = "mimetype-group-";

	@Override
	protected void executeImpl( Action action , NodeRef actionedUponNodeRef ) {
		if ( nodeService.exists( actionedUponNodeRef ) == true ) {
			try{
				ChildAssociationRef childAssociationef = nodeService.getPrimaryParent( actionedUponNodeRef );
				NodeRef parentNodeRef = childAssociationef.getParentRef();
				String name = nodeService.getProperty( actionedUponNodeRef , ContentModel.PROP_NAME ).toString();
				String parentName = nodeService.getProperty( parentNodeRef , ContentModel.PROP_NAME ).toString();
				logger.debug( "[" + MakeContainerMimeTypesSet.class.getName() + "] Processing \"" + name + "\" in \"" + parentName + "\"." );
				taggingService.clearTags( parentNodeRef );
				List<ChildAssociationRef> childrenAssoc = nodeService.getChildAssocs( parentNodeRef );
				for( ChildAssociationRef childAssoc : childrenAssoc ){
					ContentData contentData = (ContentData) nodeService.getProperty( childAssoc.getChildRef() , ContentModel.PROP_CONTENT );
					String mimeType = contentData.getMimetype();
					String mimeTypeGroup = mimeType.split( Pattern.quote( "/" ) )[0];
					if( !taggingService.hasTag( parentNodeRef , TAG_PREFIX + mimeType.replaceAll( Pattern.quote( "/" ) , "---" ) ) ) taggingService.addTag( parentNodeRef, TAG_PREFIX + mimeType.replaceAll( Pattern.quote( "/" ) , "---" ) );
					if( !taggingService.hasTag( parentNodeRef , TAG_GROUP_PREFIX + mimeTypeGroup ) ) taggingService.addTag( parentNodeRef, TAG_GROUP_PREFIX + mimeTypeGroup );
				}
			}
			catch( Exception e ){
				logger.error( "[" + MakeContainerMimeTypesSet.class.getName() + "] Process of executing " + NAME + " rule failed." );
				logger.error( "[" + MakeContainerMimeTypesSet.class.getName() + "] Error message: " + e.getMessage() );
			}
			logger.debug( "[" + MakeContainerMimeTypesSet.class.getName() + "] Rule \"" + NAME + "\" applied against node \"" + nodeService.getProperty( actionedUponNodeRef , ContentModel.PROP_NAME ) + "\"." );
		}
	}

	@Override
	protected void addParameterDefinitions( List<ParameterDefinition> paramList ) {
	}

}
