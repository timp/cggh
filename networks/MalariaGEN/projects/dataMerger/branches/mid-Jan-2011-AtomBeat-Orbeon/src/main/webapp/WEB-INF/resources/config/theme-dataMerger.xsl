<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:xhtml="http://www.w3.org/1999/xhtml"
    xmlns:version="java:org.orbeon.oxf.common.Version">

    <!-- Orbeon Forms version -->
    <xsl:variable name="orbeon-forms-version" select="version:getVersionString()" as="xs:string"/>

    <!-- - - - - - - Themed page template - - - - - - -->
    <xsl:template match="/*">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xhtml:head>
                <!-- Add meta as early as possible -->
                <xsl:apply-templates select="/xhtml:html/xhtml:head/xhtml:meta"/>
                <!-- Handle head elements except scripts -->
                <xsl:apply-templates select="/xhtml:html/xhtml:head/(xhtml:link | xhtml:style)"/>
                <!-- Title -->
                <xhtml:title>
                    <xsl:apply-templates select="/xhtml:html/xhtml:head/xhtml:title/@*"/>
                    <xsl:choose>
                        <xsl:when test="/xhtml:html/xhtml:head/xhtml:title != ''">
                            <xsl:value-of select="/xhtml:html/xhtml:head/xhtml:title"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="(/xhtml:html/xhtml:body/xhtml:h1)[1]"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xhtml:title>
                
                <xhtml:link rel="stylesheet" href="/apps/common/css/common.css" type="text/css" media="all"/>
                
                <!-- Orbeon Forms version -->
                <xhtml:meta name="generator" content="{$orbeon-forms-version}"/>
                <!-- Favicon -->
                <xhtml:link rel="shortcut icon" href="/ops/images/orbeon-icon-16.ico"/>
                <xhtml:link rel="icon" href="/ops/images/orbeon-icon-16.png" type="image/png"/>
            </xhtml:head>
            <xhtml:body>
                <!-- Copy body attributes -->
                <xsl:apply-templates select="/xhtml:html/xhtml:body/@*"/>
                <!-- Copy body -->
                <xsl:apply-templates select="/xhtml:html/xhtml:body/node()"/>
            </xhtml:body>
            <!-- Scripts at the bottom of the page. This is not valid HTML, but it is a recommended practice for
                 performance as of early 2008. See http://developer.yahoo.com/performance/rules.html#js_bottom -->
            <xsl:for-each select="/xhtml:html/xhtml:head/xhtml:script">
                <xsl:element name="xhtml:{local-name()}" namespace="{namespace-uri()}">
                    <xsl:apply-templates select="@*|node()"/>
                </xsl:element>
            </xsl:for-each>
        </xsl:copy>
    </xsl:template>

    <!-- Simply copy everything that's not matched -->
    <xsl:template match="@*|node()" priority="-2">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
