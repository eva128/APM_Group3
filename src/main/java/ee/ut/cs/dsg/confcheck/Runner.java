package ee.ut.cs.dsg.confcheck;

import ee.ut.cs.dsg.confcheck.alignment.Alignment;
import ee.ut.cs.dsg.confcheck.alignment.AlignmentFactory;
import ee.ut.cs.dsg.confcheck.trie.Trie;
import ee.ut.cs.dsg.confcheck.trie.TrieNode;
import ee.ut.cs.dsg.confcheck.util.AlphabetService;
import ee.ut.cs.dsg.confcheck.util.Configuration;
import ee.ut.cs.dsg.confcheck.StatefulRandomConformanceChecker;
import ee.ut.cs.dsg.confcheck.util.Utils;
import gnu.trove.impl.sync.TSynchronizedShortByteMap;
import gnu.trove.impl.sync.TSynchronizedShortCharMap;
import lpsolve.LpSolve;
import lpsolve.LpSolveException;
import org.apache.commons.math3.analysis.function.Add;
import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.out.XesXmlSerializer;
import org.processmining.logfiltering.algorithms.ProtoTypeSelectionAlgo;
import org.processmining.logfiltering.legacy.plugins.logfiltering.enumtypes.PrototypeType;
import org.processmining.logfiltering.legacy.plugins.logfiltering.enumtypes.SimilarityMeasure;
import org.processmining.logfiltering.parameters.MatrixFilterParameter;
import org.processmining.logfiltering.parameters.SamplingReturnType;
import org.processmining.models.connections.GraphLayoutConnection;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetImpl;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.pnml.base.FullPnmlElementFactory;
import org.processmining.plugins.pnml.base.Pnml;
import org.processmining.plugins.pnml.elements.extensions.opennet.PnmlLabel;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import static ee.ut.cs.dsg.confcheck.util.Configuration.ConformanceCheckerType;
import static ee.ut.cs.dsg.confcheck.util.Configuration.LogSortType;
public class Runner {

    private static int totalCostTree = 0;
    private static double totalCostBaseline = 0;

    private static AlphabetService service;

    public static void main(String... args)
    {
//        testBed2();
//        System.exit(0);
//        testBed1();
//        testConformanceApproximation();
//
//        testJNI();

//        testBed3();
//        System.exit(0);
        String path = "C:\\Users\\ebeha\\Documents\\Uni Mannheim\\3. Semester\\IE 692 Advanced Process Mining\\Group Project\\";

        String randomProxyLog = path + "\\BPI2015\\randomLog.xml";
        String clusteredLog = path +  "\\BPI2015\\sampledClusteredLog.xml";
        String simulatedLog = path +  "\\BPI2015\\simulatedLog.xml";
        String reducedActivityLog = path +  "\\BPI2015\\reducedLogActivity.xml";
        String frequencyActivityLog = path +  "\\BPI2015\\frequencyLog.xml";
        String sampleLog = path +  "\\BPI2015\\sampledLog.xml";
        String singular = path +  "\\BPI2015\\Singular.xes";

        String randomSepsisProxyLog = path +  "\\Sepsis\\randomLog.xml";
        String clusteredSepsisLog = path +  "\\Sepsis\\sampledClusteredLog.xml";
        String simulatedSepsisLog = path +  "\\Sepsis\\simulatedLog.xml";
        String frequencySepsisLog = path +  "\\Sepsis\\frequencyLog.xml";
        String reducedSepsisActivityLog = path +  "\\Sepsis\\reducedLogActivity.xml";
        String sampleSepsisLog = path +  "\\Sepsis\\sampledLog.xml";

        // BPI 2019
        String originalLog2019 = path +  "\\BPI2019\\BPI_Challenge_2019.xml";
        String random2019ProxyLog = path +  "\\BPI2019\\randomLog.xml";
        String clustered2019Log = path +  "\\BPI2019\\sampledClusteredLog.xml";
        String simulated2019Log = path +  "\\BPI2019\\simulatedLog.xml";
        String reduced2019ActivityLog = path +  "\\BPI2019\\reducedLogActivity.xml";
        String sample2019Log = path +  "\\BPI2019\\sampledLog.xml";
        String frequency2019Log = path +  "\\BPI2019\\frequencyLog.xml";

        // BPI 2012
        String originalLog2012 = path +  "\\BPI2012\\BPIC2012.xes";
        String random2012ProxyLog = path +  "\\BPI2012\\randomLog.xml";
        String clustered2012Log = path +  "\\BPI2012\\sampledClusteredLog.xml";
        String simulated2012Log = path +  "\\BPI2012\\simulatedLog.xml";
        String reduced2012ActivityLog = path +  "\\BPI2012\\reducedLogActivity.xml";
        String sample2012Log = path +  "\\BPI2012\\sampledLog.xml";
        String frequency2012Log = path +  "\\BPI2012\\frequencyLog.xml";

        // BPI 2017
        String originalLog2017 = path +  "\\BPI2017\\BPIC2017.xes.xes";
        String random2017ProxyLog = path +  "\\BPI2017\\rand_randomLog.xml";
        String clustered2017Log = path +  "\\BPI2017\\sampledClusteredLog.xml";
        String simulated2017Log = path +  "\\BPI2017\\simulatedLog.xml";
        String reduced2017ActivityLog = path +  "\\BPI2017\\reducedLogActivity.xml";
        String sample2017Log = path +  "\\BPI2017\\sampledLog.xml";
        String frequency2017Log = path +  "\\BPI2017\\freq_frequencyLog.xml";


        // testOnConformanceApproximationResults(frequencyActivityLog, sampleLog, ConformanceCheckerType.TRIE_RANDOM_STATEFUL, LogSortType.TRACE_LENGTH_ASC );
        //Trie based
        testOnConformanceApproximationResults(simulated2012Log, sample2012Log, ConformanceCheckerType.TRIE_RANDOM_STATEFUL, LogSortType.TRACE_LENGTH_ASC );

        // Baseline
        testVanellaConformanceApproximation(simulated2012Log,sample2012Log);
        //The frequency log is the Trie/ Proxy Behavior M’ <- first parameter, 15 for 2015, 1200 for 2019
        //The SampleLog is the InputLog <- second parameter, 100 for 2015+2019

        //by Eva --> calculate mae
        double mae = (totalCostBaseline - totalCostTree) / 101;
        System.out.println(mae);

//        // BPI 2015
//        printLogStatistics(simulatedLog);
////        printLogStatistics(sampleLog);
//        printLogStatistics(clusteredLog);
//        printLogStatistics(randomProxyLog);
//        printLogStatistics(frequencyActivityLog);
//        printLogStatistics(reducedActivityLog);

//        // BPI 2012
//        printLogStatistics(simulated2012Log);
////        printLogStatistics(sample2012Log);
//        printLogStatistics(clustered2012Log);
//        printLogStatistics(random2012ProxyLog);
//        printLogStatistics(frequency2012Log);
//        printLogStatistics(reduced2012ActivityLog);

//        // BPI 2017
//        printLogStatistics(simulated2017Log);
////        printLogStatistics(sample2017Log);
//        printLogStatistics(clustered2017Log);
//        printLogStatistics(random2017ProxyLog);
//        printLogStatistics(frequency2017Log);
//        printLogStatistics(reduced2017ActivityLog);

//        // BPI 2019
//        printLogStatistics(simulated2019Log);
////        printLogStatistics(sample2019Log);
//        printLogStatistics(clustered2019Log);
//        printLogStatistics(random2019ProxyLog);
//        printLogStatistics(frequency2019Log);
//        printLogStatistics(reduced2019ActivityLog);

        //SEPSIS
//        printLogStatistics(simulatedSepsisLog);
////        printLogStatistics(sampleSepsisLog);
//        printLogStatistics(clusteredSepsisLog);
//        printLogStatistics(randomSepsisProxyLog);
//        printLogStatistics(frequencySepsisLog);
//        printLogStatistics(reducedActivityLog);
    }

    private static void testBed2()
    {
        List<String> trace = new ArrayList<>();
        trace.add("a");
        trace.add("c");
        trace.add("b");
        trace.add("e");

        List<String> trace2 = new ArrayList<>();
        trace2.add("a");
        trace2.add("b");
        trace2.add("c");
        trace2.add("d");
        trace2.add("b");
        trace2.add("e");

        List<String> trace3 = new ArrayList<>();
        trace3.add("a");
        trace3.add("b");
        trace3.add("c");
        trace3.add("d");
        trace3.add("b");
        trace3.add("d");
        trace3.add("b");
        trace3.add("e");

        List<String> trace4 = new ArrayList<>();
        trace4.add("a");
        trace4.add("c");
        trace4.add("b");
        trace4.add("e");

        List<String> trace5 = new ArrayList<>();
        trace5.add("a");
        trace5.add("b");
        trace5.add("e");

        Trie t = new Trie(28);
        t.addTrace(trace);
        t.addTrace(trace2);
        t.addTrace(trace3);
        t.addTrace(trace4);
        t.addTrace(trace5);

//        System.out.println(t.toString());
        // Now log traces

        // we can reuse trace 4
        List<String> trace6 = new ArrayList<>();
        trace6.add("a");
        trace6.add("e");

        List<String> trace7 = new ArrayList<>();
        trace7.add("a");
        trace7.add("c");
        trace7.add("b");
        trace7.add("d");
        trace7.add("e");



        List<String> trace8 = new ArrayList<>();
        trace8.add("c");
        trace8.add("e");

        List<String> trace9 = new ArrayList<>();
        trace9.add("xxx");
        trace9.add("z");
        trace9.add("e");

        ConformanceChecker cnfChecker;// = new ConformanceChecker(t);
        cnfChecker = new StatefulRandomConformanceChecker(t,1,1,1000, 100000);

        Alignment alg;
        System.out.println(trace4.toString());
        alg = cnfChecker.check(trace4);
        System.out.println(alg.toString());

        System.out.println(trace6.toString());
        alg = cnfChecker.check(trace6);
        System.out.println(alg.toString());

        System.out.println(trace7.toString());
        alg = cnfChecker.check(trace7);
        System.out.println(alg.toString());

        alg = cnfChecker.check(trace7);
        System.out.println(alg.toString());

//
        System.out.println(trace8.toString());
        alg = cnfChecker.check(trace8);
        System.out.println(alg.toString());
//
//        long start = System.currentTimeMillis();
//        System.out.println(trace9.toString());
//        alg = cnfChecker.check(trace9);
//        System.out.println(alg.toString());
//        System.out.println(String.format("Time taken: %d ms", System.currentTimeMillis()-start));


    }

    private static Pnml importPnmlFromStream(InputStream input) throws
            XmlPullParserException, IOException {
        FullPnmlElementFactory pnmlFactory = new FullPnmlElementFactory();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(input, null);
        int eventType = xpp.getEventType();
        Pnml pnml = new Pnml();
        synchronized (pnmlFactory) {
            pnml.setFactory(pnmlFactory);
            /*
             * Skip whatever we find until we've found a start tag.
             */
            while (eventType != XmlPullParser.START_TAG) {
                eventType = xpp.next();
            }
            /*
             * Check whether start tag corresponds to PNML start tag.
             */
            if (xpp.getName().equals(Pnml.TAG)) {
                /*
                 * Yes it does. Import the PNML element.
                 */
                pnml.importElement(xpp, pnml);
            } else {
                /*
                 * No it does not. Return null to signal failure.
                 */
                pnml.log(Pnml.TAG, xpp.getLineNumber(), "Expected pnml");
            }
            if (pnml.hasErrors()) {
                return null;
            }
            return pnml;
        }
    }

    private static void init()
    {
        service = new AlphabetService();
    }


    private static void printLogStatistics(String inputLog)
    {
        init();
        long startTs = System.currentTimeMillis();
        Trie t = constructTrie(inputLog);
        long endTs = System.currentTimeMillis();

        System.out.println(String.format("Stats for trace from %s", inputLog));
        System.out.println(String.format("Max length of a trace %d", t.getMaxTraceLength()));
        System.out.println(String.format("Min length of a trace %d", t.getMinTraceLength()));
        System.out.println(String.format("Avg length of a trace %d", t.getAvgTraceLength()));
        System.out.println(String.format("Number of nodes in the trie %d", t.getSize()));
        System.out.println(String.format("Total number of events %d", t.getNumberOfEvents()));
        System.out.println(String.format("Trie construction time %d ms", (endTs-startTs)));
    }


    private static void testOnConformanceApproximationResults(String inputProxyLogFile, String inputSampleLogFile, ConformanceCheckerType confCheckerType, LogSortType sortType)
    {
        init();
        Trie t = constructTrie(inputProxyLogFile);

        //Configuration variables

        boolean sortTraces=true;

//      t.printTraces();
//        System.out.println(t);
        XLog inputSamplelog;
        XEventClass dummyEvClass = new XEventClass("DUMMY", 99999);
        XEventClassifier eventClassifier = XLogInfoImpl.NAME_CLASSIFIER;
        XesXmlParser parser = new XesXmlParser();

        try{
            InputStream is = new FileInputStream(inputSampleLogFile);
            inputSamplelog = parser.parse(is).get(0);


            List<String> templist = new ArrayList<>();
            List<String> tracesToSort = new ArrayList<>();
           // AlphabetService service = new AlphabetService();

            ConformanceChecker checker;
            if (confCheckerType == ConformanceCheckerType.TRIE_PREFIX)
                          checker = new PrefixConformanceChecker(t,1,1, false);
            else if (confCheckerType == ConformanceCheckerType.TRIE_RANDOM)
                checker = new RandomConformanceChecker(t,1,1, 50000, 100000);//Integer.MAX_VALUE);
            else if (confCheckerType == ConformanceCheckerType.TRIE_RANDOM_STATEFUL)
                checker = new StatefulRandomConformanceChecker(t,1,1, 50000, 100000);//Integer.MAX_VALUE);
            else
            {
                testVanellaConformanceApproximation(inputProxyLogFile,inputSampleLogFile);
                return;
            }

            Alignment alg;
            HashMap<String, Integer> sampleTracesMap = new HashMap<>();
            long start;
            long totalTime=0;
            int skipTo =0;
            int current = -1;
            int takeTo = 100;
            DeviationChecker devChecker = new DeviationChecker(service);
            int cnt = 1;
            for (XTrace trace: inputSamplelog)
            {
                current++;
                if (current < skipTo)
                    continue;
                if (current> takeTo)
                    break;
                templist = new ArrayList<String>();

                for (XEvent e: trace)
                {
                    String label = e.getAttributes().get(inputSamplelog.getClassifiers().get(0).getDefiningAttributeKeys()[0]).toString();
                    templist.add(Character.toString(service.alphabetize(label)));
                }
//                System.out.println(templist.toString());

                StringBuilder sb = new StringBuilder(templist.size());
                sb.append(cnt).append((char)63); // we prefix the trace with its ID

                Arrays.stream(templist.toArray()).forEach( e-> sb.append(e));

                sampleTracesMap.put(sb.toString(),cnt);
                cnt++;

                tracesToSort.add(sb.toString());
            }
            if (confCheckerType == ConformanceCheckerType.TRIE_RANDOM_STATEFUL) {

                if (sortType == LogSortType.TRACE_LENGTH_ASC || sortType == LogSortType.TRACE_LENGTH_DESC)
                    tracesToSort.sort(Comparator.comparingInt(String::length));
                else if (sortType == LogSortType.LEXICOGRAPHIC_ASC || sortType == LogSortType.LEXICOGRAPHIC_DESC)
                    Collections.sort(tracesToSort);
            }

            System.out.println("Trace#, Alignment cost");

            if (sortType == LogSortType.LEXICOGRAPHIC_DESC || sortType == LogSortType.TRACE_LENGTH_DESC)
            {

                for (int i = tracesToSort.size() -1; i>=0; i--)
                {
                    totalTime = computeAlignment(tracesToSort, checker, sampleTracesMap, totalTime, devChecker, i);
                }
            }
//
            else {
                for (int i = 0; i < tracesToSort.size(); i++) {
                    totalTime = computeAlignment(tracesToSort, checker, sampleTracesMap, totalTime, devChecker, i);
                }
            }


            System.out.println(String.format("Time taken for trie-based conformance checking %d milliseconds",totalTime));
            System.out.println(totalCostTree);

//            for (String label: devChecker.getAllActivities())
//            {
//                System.out.println(String.format("%s, %f",label, devChecker.getDeviationPercentage(label)));
//            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static long computeAlignment(List<String> tracesToSort, ConformanceChecker checker, HashMap<String, Integer> sampleTracesMap, long totalTime, DeviationChecker devChecker, int i) {
        long start;
        Alignment alg;
        List<String> trace = new ArrayList<String>();

        int pos = tracesToSort.get(i).indexOf((char)63);
        int traceNum = Integer.parseInt(tracesToSort.get(i).substring(0,pos));

        String actualTrace = tracesToSort.get(i).substring(pos+1);
//        System.out.println(actualTrace);
        for (char c : actualTrace.toCharArray()) {
            trace.add(new StringBuilder().append(c).toString());
        }
        start = System.currentTimeMillis();
        alg = checker.check(trace);
        totalTime += System.currentTimeMillis() - start;
        if (alg != null) {
            System.out.print(sampleTracesMap.get(tracesToSort.get(i)));
            System.out.println(", " + alg.getTotalCost());
            totalCostTree = totalCostTree + alg.getTotalCost();
//                        System.out.println(alg.toString(service));
//            devChecker.processAlignment(alg);
//                    System.out.println(alg.toString());
//                        t.printTraces();
        } else //if (usePrefixChecker == false)
            System.out.println("Couldn't find an alignment under the given constraints");
        return totalTime;
    }

    private static XLog loadLog(String inputProxyLogFile)
    {
        XLog inputProxyLog;//, inputSamplelog;
        XEventClass dummyEvClass = new XEventClass("DUMMY", 99999);
        XEventClassifier eventClassifier = XLogInfoImpl.NAME_CLASSIFIER;
        XesXmlParser parser = new XesXmlParser();

        try {
            InputStream is = new FileInputStream(inputProxyLogFile);
            inputProxyLog = parser.parse(is).get(0);
//            XLogInfo logInfo = inputProxyLog.getInfo(eventClassifier);
//            logInfo = XLogInfoFactory.createLogInfo(inputProxyLog, inputProxyLog.getClassifiers().get(0));
            return inputProxyLog;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    private static Trie constructTrie(String inputProxyLogFile)
    {
        XLog inputProxyLog = loadLog(inputProxyLogFile);
        XEventClass dummyEvClass = new XEventClass("DUMMY", 99999);
        XEventClassifier eventClassifier = XLogInfoImpl.NAME_CLASSIFIER;


        try {

            XLogInfo logInfo ;
            logInfo = XLogInfoFactory.createLogInfo(inputProxyLog, inputProxyLog.getClassifiers().get(0));
            int count = 0;
            for (XEventClass clazz : logInfo.getNameClasses().getClasses()) {
                count++;
                //        System.out.println(clazz.toString());
            }
//            System.out.println("Number of unique activities " + count);

            //Let's construct the trie from the proxy log
            Trie t = new Trie(count);
            List<String> templist;
//            count=1;
            count=1;
//            System.out.println("Proxy log size "+inputProxyLog.size());
            for (XTrace trace : inputProxyLog) {
                templist = new ArrayList<String>();
                for (XEvent e : trace) {
                    String label = e.getAttributes().get(inputProxyLog.getClassifiers().get(0).getDefiningAttributeKeys()[0]).toString();

                    templist.add(Character.toString(service.alphabetize(label)));
                }
//                count++;
                //System.out.println(templist.toString());
                if (templist.size() > 0 ) {

//                    System.out.println(templist.toString());
//                    if (count == 37)
//                    StringBuilder sb = new StringBuilder();
//                    templist.stream().forEach(e -> sb.append(e));
//                    System.out.println(sb.toString());
                    t.addTrace(templist);
//                    if (count ==5)
//                    break;
                }
                count++;
            }
            return t;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    private static void testVanellaConformanceApproximation(String inputProxyLogFile, String inputSampleLogFile)
    {
        XLog proxyLog, sampleLog;
        StringBuilder sb;
        List<String> proxyTraces = new ArrayList<>();
        List<String> sampleTraces = new ArrayList<>();
        proxyLog = loadLog(inputProxyLogFile);
        sampleLog = loadLog(inputSampleLogFile);
        HashMap<String, Integer> sampleTracesMap = new HashMap<>();
        init();

        for (XTrace trace : proxyLog) {
            sb = new StringBuilder();
            for (XEvent e : trace) {
                String label = e.getAttributes().get(proxyLog.getClassifiers().get(0).getDefiningAttributeKeys()[0]).toString();

                sb.append(service.alphabetize(label));
            }
            proxyTraces.add(sb.toString());

        }
        int cnt=1;
        for (XTrace trace : sampleLog) {
            sb = new StringBuilder();
            for (XEvent e : trace) {
                String label = e.getAttributes().get(sampleLog.getClassifiers().get(0).getDefiningAttributeKeys()[0]).toString();

                sb.append(service.alphabetize(label));
            }
            sampleTraces.add(sb.toString());
            sampleTracesMap.put(sb.toString(),cnt);
            cnt++;
        }

        DeviationChecker deviationChecker = new DeviationChecker(service);
        // Now compute the alignments
        long start=System.currentTimeMillis(),timeTaken=0 ;
        int skipTo =0;
        int current = -1;
        int takeTo = 100;
        try {
            System.out.println("Trace#, Alignment cost");

            for (String logTrace : sampleTraces) {
                current++;
                if (current < skipTo)
                    continue;
                if (current > takeTo)
                    break;
                double minCost = Double.MAX_VALUE;
                String bestTrace = "";
                String bestAlignment = "";
                start = System.currentTimeMillis();
                for (String proxyTrace : proxyTraces) {

                    ProtoTypeSelectionAlgo.AlignObj obj = ProtoTypeSelectionAlgo.levenshteinDistancewithAlignment(logTrace, proxyTrace);
                    if (obj.cost < minCost) {
                        minCost = obj.cost;
                        bestAlignment = obj.Alignment;
                        bestTrace = proxyTrace;
                        if (obj.cost == 0)
                            break;
                    }
                }
                timeTaken += System.currentTimeMillis() - start;
//            System.out.println("Total proxy traces "+proxyTraces.size());
//            System.out.println("Total candidate traces to inspect "+proxyTraces.size());
                //print trace number
                System.out.print(sampleTracesMap.get(logTrace));
                // print cost
                System.out.println(", " + minCost);

                // by Eva
                totalCostBaseline = totalCostBaseline + minCost;

//            System.out.println(bestAlignment);
//                Alignment alg = AlignmentFactory.createAlignmentFromString(bestAlignment);
//              System.out.println(alg.toString());
//                deviationChecker.processAlignment(alg);
//            System.out.println("Log trace "+logTrace);
//            System.out.println("Aligned trace "+bestTrace);
//            System.out.println("Trace number "+sampleTracesMap.get(bestTrace));

            }

            //by Eva
            System.out.println(totalCostBaseline);

            System.out.println(String.format("Time taken for Distance-based approximate conformance checking %d milliseconds", timeTaken ));

//            for (String label: deviationChecker.getAllActivities())
//            {
//                System.out.println(String.format("%s, %f",label, deviationChecker.getDeviationPercentage(label)));
//            }

        }
        catch (Exception e)
        {
            System.out.println(String.format("Time taken for Distance-based approximate conformance checking %d milliseconds", System.currentTimeMillis() - start));
            e.printStackTrace();

        }

    }
    private static void testConformanceApproximation()
    {
        //This method is used to test the approach by Fani Sani
        XEventClass dummyEvClass = new XEventClass("DUMMY", 99999);
        XEventClassifier eventClassifier = XLogInfoImpl.NAME_CLASSIFIER;
        XesXmlParser parser = new XesXmlParser();
        XLog inputLog;

        try {
            InputStream is = new FileInputStream("C:\\Work\\DSG\\Data\\BPI2015Reduced2014.xml");
            inputLog = parser.parse(is).get(0);
            Pnml pnml = importPnmlFromStream(new FileInputStream("C:\\Work\\DSG\\Data\\IM_Petrinet.pnml"));
            Petrinet pn = PetrinetFactory.newPetrinet(pnml.getLabel());
            Marking imk=new Marking();
            Collection<Marking> fmks = new HashSet<>();
            GraphLayoutConnection glc = new GraphLayoutConnection(pn);
            pnml.convertToNet(pn,imk, fmks,glc);
            MatrixFilterParameter parameter = new MatrixFilterParameter(10, inputLog.getClassifiers().get(0), SimilarityMeasure.Levenstein, SamplingReturnType.Traces, PrototypeType.KMeansClusteringApprox);
            //now the target
            String result = ProtoTypeSelectionAlgo.apply(inputLog,pn,parameter,null);

            System.out.println(result);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private static void testJNI()
    {
        try {
            // Create a problem with 4 variables and 0 constraints
            LpSolve solver = LpSolve.makeLp(0, 4);

            // add constraints
            solver.strAddConstraint("3 2 2 1", LpSolve.LE, 4);
            solver.strAddConstraint("0 4 3 1", LpSolve.GE, 3);

            // set objective function
            solver.strSetObjFn("2 3 -2 3");

            // solve the problem
            solver.solve();

            // print solution
            System.out.println("Value of objective function: " + solver.getObjective());
            double[] var = solver.getPtrVariables();
            for (int i = 0; i < var.length; i++) {
                System.out.println("Value of var[" + i + "] = " + var[i]);
            }

            // delete the problem and free memory
            solver.deleteLp();
        }
        catch (LpSolveException e) {
            e.printStackTrace();
        }
    }
    private static void testBed1() {
        List<String> trace = new ArrayList<>();
        trace.add("a");
        trace.add("b");
        trace.add("c");
        trace.add("d");

        List<String> trace2 = new ArrayList<>();
        trace2.add("a");
        trace2.add("b");
        trace2.add("x");
        trace2.add("e");

        List<String> trace3 = new ArrayList<>();
        trace3.add("e");
        trace3.add("v");
        trace3.add("d");
        trace3.add("e");


        List<String> trace4 = new ArrayList<>();
        trace4.add("a");
        trace4.add("b");
        trace4.add("c");

        List<String> trace5 = new ArrayList<>();
        trace5.add("a");
        trace5.add("e");

        Trie t = new Trie(28);
        t.addTrace(trace);
        t.addTrace(trace2);
        t.addTrace(trace3);
        t.addTrace(trace4);
        t.addTrace(trace5);

//        List<String> trace6 = new ArrayList<>();
//        trace6.add("a");
//        trace6.add("b");
//        trace6.add("c");
//        trace6.add("d");
//        trace6.add("e");

        List<String> trace6 = new ArrayList<>();
        trace6.add("e");
//        trace6.add("b");
//        trace6.add("c");
//        trace6.add("d");
        trace6.add("x");

//        System.out.println(t.toString());
        ConformanceChecker cnfChecker = new RandomConformanceChecker(t,1,1,1000);

        Alignment alg = cnfChecker.check(trace6);

        System.out.println(alg.toString());
    }

    private static void testBed3()
    {
        String model = "AEFwBCDCJIKLOMlmGonpqtMrZuvN\u0081OPQRSTUV\\[`WabHXgYcdfeheji";
        String trace = "FGHBCDEAJICKLMlmOonptqusrvNOPQRSTUVWabMhg[i^Y\\_]c`eXZjdfe";

        List<String> modelTrace = new ArrayList<>(model.length());


        for (char c : model.toCharArray())
        {
            modelTrace.add(String.valueOf(c));
        }

        List<String> traceTrace = new ArrayList<>(model.length());


        for (char c : trace.toCharArray())
        {
            traceTrace.add(String.valueOf(c));
        }

        Trie t = new Trie(100);
        t.addTrace(modelTrace);
        long start = System.currentTimeMillis();
        ConformanceChecker cnfChecker = new RandomConformanceChecker(t,1,1,100000);
        Alignment alg = cnfChecker.check(traceTrace);
        long total = System.currentTimeMillis() - start;
        System.out.println(alg.toString());
        System.out.println(String.format("Total time %d", total));


    }
}
