xquery version "1.0";

module namespace config-collections = "http://purl.org/atombeat/xquery/config-collections";

declare namespace atom = "http://www.w3.org/2005/Atom" ;
declare namespace atombeat = "http://purl.org/atombeat/xmlns" ;

declare variable $config-collections:collection-spec := 
    <spec>

        <collection path-info="/media/uploaded">
            <atom:feed
                atombeat:enable-versioning="false"
                atombeat:exclude-entry-content="false"
                atombeat:recursive="true">
                <atom:title type="text">Uploaded files</atom:title>
            </atom:feed>
        </collection>  

        <collection path-info="/media/merged">
            <atom:feed
                atombeat:enable-versioning="false"
                atombeat:exclude-entry-content="false"
                atombeat:recursive="true">
                <atom:title type="text">Merged files</atom:title>
            </atom:feed>
        </collection>      
        
    </spec>
;
