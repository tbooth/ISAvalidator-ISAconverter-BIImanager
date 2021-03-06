/**

 The ISAconverter, ISAvalidator & BII Management Tool are components of the ISA software suite (http://www.isa-tools.org)

 Exhibit A
 The ISAconverter, ISAvalidator & BII Management Tool are licensed under the Mozilla Public License (MPL) version
 1.1/GPL version 2.0/LGPL version 2.1

 "The contents of this file are subject to the Mozilla Public License
 Version 1.1 (the "License"). You may not use this file except in compliance with the License.
 You may obtain copies of the Licenses at http://www.mozilla.org/MPL/MPL-1.1.html.

 Software distributed under the License is distributed on an "AS IS"
 basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 License for the specific language governing rights and limitations
 under the License.

 The Original Code is the ISAconverter, ISAvalidator & BII Management Tool.

 The Initial Developer of the Original Code is the ISA Team (Eamonn Maguire, eamonnmag@gmail.com;
 Philippe Rocca-Serra, proccaserra@gmail.com; Susanna-Assunta Sansone, sa.sanson@gmail.com;
 http://www.isa-tools.org). All portions of the code written by the ISA Team are Copyright (c)
 2007-2011 ISA Team. All Rights Reserved.

 Contributor(s):
 Rocca-Serra P, Brandizi M, Maguire E, Sklyar N, Taylor C, Begley K, Field D,
 Harris S, Hide W, Hofmann O, Neumann S, Sterk P, Tong W, Sansone SA. ISA software suite:
 supporting standards-compliant experimental annotation and enabling curation at the community level.
 Bioinformatics 2010;26(18):2354-6.

 Alternatively, the contents of this file may be used under the terms of either the GNU General
 Public License Version 2 or later (the "GPL") - http://www.gnu.org/licenses/gpl-2.0.html, or
 the GNU Lesser General Public License Version 2.1 or later (the "LGPL") -
 http://www.gnu.org/licenses/lgpl-2.1.html, in which case the provisions of the GPL
 or the LGPL are applicable instead of those above. If you wish to allow use of your version
 of this file only under the terms of either the GPL or the LGPL, and not to allow others to
 use your version of this file under the terms of the MPL, indicate your decision by deleting
 the provisions above and replace them with the notice and other provisions required by the
 GPL or the LGPL. If you do not delete the provisions above, a recipient may use your version
 of this file under the terms of any one of the MPL, the GPL or the LGPL.

 Sponsors:
 The ISA Team and the ISA software suite have been funded by the EU Carcinogenomics project
 (http://www.carcinogenomics.eu), the UK BBSRC (http://www.bbsrc.ac.uk), the UK NERC-NEBC
 (http://nebc.nerc.ac.uk) and in part by the EU NuGO consortium (http://www.nugo.org/everyone).

 */

package org.isatools.isatab.export.isatab.investigationFile;

import org.isatools.isatab.export.isatab.ISATABExporter;
import org.isatools.isatab.export.properties.OntologyEntryExportingHelper;
import org.isatools.isatab.mapping.AssayGroup;
import org.isatools.isatab_v1.ISATABLoader;
import org.isatools.tablib.export.StringPropertyComparator;
import org.isatools.tablib.export.properties.PropertyExportingHelper;
import org.isatools.tablib.export.properties.StringExportingHelper;
import org.isatools.tablib.schema.Section;
import org.isatools.tablib.utils.BIIObjectStore;
import uk.ac.ebi.bioinvindex.model.Study;
import uk.ac.ebi.bioinvindex.model.term.AssayTechnology;
import uk.ac.ebi.bioinvindex.model.term.Measurement;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Rebuilds the assay section in the investigation file, the corresponging assay groups are computed by the
 * {@link ISATABExporter}, from the assays in the studies to be exported.
 *
 * @author brandizi
 *         <b>date</b>: Mar 11, 2010
 */
public class AssayGroupTabExporter extends StudyComponentTabExporter<AssayGroup> {
    public AssayGroupTabExporter(BIIObjectStore store) {
        super(store);
        propertyExportingHelpers = new LinkedList<PropertyExportingHelper<AssayGroup>>();
        propertyExportingHelpers.add(new OntologyEntryExportingHelper<AssayGroup, Measurement>(store,
                "measurement",
                "Study Assay Measurement Type",
                "Study Assay Measurement Type Term Accession Number",
                "Study Assay Measurement Type Term Source REF"
        ));
        propertyExportingHelpers.add(new OntologyEntryExportingHelper<AssayGroup, AssayTechnology>(store,
                "technology",
                "Study Assay Technology Type",
                "Study Assay Technology Type Term Accession Number",
                "Study Assay Technology Type Term Source REF"
        ));
        propertyExportingHelpers.add(new StringExportingHelper<AssayGroup>(store, "platform", "Study Assay Technology Platform"));
        propertyExportingHelpers.add(new StringExportingHelper<AssayGroup>(store, "filePath", "Study Assay File Name"));

        this.comparator = new StringPropertyComparator<AssayGroup>("acc");
    }

    @Override
    protected Section getSection() {
        return ISATABLoader.getISATABSchema().getDescendant("assays", Section.class);
    }

    @Override
    protected Collection<AssayGroup> getSources() {
        Study study = getCurrentSource();
        Set<AssayGroup> result = new HashSet<AssayGroup>();
        for (AssayGroup ag : getStore().valuesOfType(AssayGroup.class)) {
            if (ag.getStudy() == study) {
                result.add(ag);
            }
        }
        return result;
    }

}
