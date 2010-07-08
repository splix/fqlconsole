package com.github.splix.fqlconsole

import com.google.code.facebookapi.*
import com.vaadin.Application
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.TabSheet.Tab
import org.json.*
import com.vaadin.ui.*
import com.vaadin.data.Property.ValueChangeListener
import com.vaadin.data.Property.ValueChangeEvent

/**
 * Facebook Query Language client console
 * 
 * @author <a href="http://igorartamonov.com">Igor Artamonov</a>
 * @since 02.07.2010
 */

public class FQLConsoleApplication extends Application implements TabSheet.CloseHandler{

    Properties props = new Properties()
    File propsFile = new File("./fqlconsole.config")
    Window window
    TabSheet results
    int queryCounter = 0

    @Override
    public void init() {
        if (!propsFile.exists()) propsFile.createNewFile()
        props.load(propsFile.newReader())

        window = new Window("Facebook Query Console")
        setMainWindow(window)

        Panel keys = new Panel("Connect keys")
        GridLayout form = new GridLayout(2,2)
        form.setMargin(true)
        form.setSpacing(true) 

        TextField appKeyFld = new TextField("App Key", props["facebook.app.key"] ? props["facebook.app.key"] : "")
        form.addComponent(appKeyFld, 0, 0)
        TextField appSecretFld = new TextField("App Secret", props["facebook.app.secret"] ? props["facebook.app.secret"] : "")
        form.addComponent(appSecretFld, 0, 1)
        TextField userSessionFld = new TextField("User Session", props["facebook.user.session"] ? props["facebook.user.session"] : "")
        form.addComponent(userSessionFld, 1, 0)
        keys.setContent(form)
        window.addComponent(keys) 

        Panel query = new Panel("Query")
        TextField queryFld = new TextField("Message")
        queryFld.setRows(5)
        queryFld.setWidth("100%")
        query.addComponent(queryFld)

        Button runQueryBtn = new Button("Execute Query")
        query.addComponent(runQueryBtn)

        results = new TabSheet()
        results.setHeight("300px") 
        results.setWidth("100%")
        results.setCloseHandler(this)
        query.addComponent(results) 

        window.addComponent(query)

        runQueryBtn.addListener({ClickEvent event ->
            executeQuery(queryFld.value)
        } as ClickListener)
        ValueChangeListener confListener = {ValueChangeEvent event ->
            saveKeys(appKeyFld.value, appSecretFld.value, userSessionFld.value)
        } as ValueChangeListener
        [appKeyFld, appSecretFld, userSessionFld].each { TextField it ->
            it.addListener(confListener)
        }
    }

    void saveKeys(String appKey, String appSecret, String userSession) {
        props.put("facebook.app.key", appKey)
        props.put("facebook.app.secret", appSecret)
        props.put("facebook.user.session", userSession)
        props.store(propsFile.newWriter(), "UTF-8")
    }

    void executeQuery(String query) {
        int index = this.queryCounter++
        try {
            IFacebookRestClient client =  new FacebookJsonRestClient(
                    props["facebook.app.key"],
                    props["facebook.app.secret"],
                    props["facebook.user.session"]
            )
            def result = client.fql_query(query)
            Accordion resultVals = new Accordion()
            resultVals.setWidth("100%")
            resultVals.setHeight("100%") 
            if (result instanceof JSONArray && result.length() > 0) {
                Table table = new Table("Query result")
                table.setWidth("100%")
                table.setHeight("100%")
                table.setSelectable(true)
                table.setMultiSelect(true)
                table.setColumnReorderingAllowed(true)
                table.setColumnCollapsingAllowed(true)

                String[] fields = JSONObject.getNames(result.getJSONObject(0))
                fields.each {
                    table.addContainerProperty(it, String,  null);
                }
                result.myArrayList.eachWithIndex { JSONObject row, int i ->
                    String[] data = fields.collect { row.myHashMap.get(it).toString() }
                    table.addItem(data, i)
                }
                resultVals.addTab(table, "Results", null)
            }
            Label jsonLbl = new Label(result.toString())
            Label queryLbl = new Label(query)
            resultVals.addTab(jsonLbl, "JSON Responce", null)
            resultVals.addTab(queryLbl, "Query", null)

            Tab resultTab = results.addTab(resultVals, "Query $index", null)
            resultTab.closable = true
            results.setSelectedTab(resultVals)
        } catch (FacebookException e) {
            Label x = new Label("Query failed with message: $e.message")
            Tab resultTab = results.addTab(x, "Failed $index", null)
            resultTab.closable = true
            results.setSelectedTab(x)
        }
    }
    
    void onTabClose(TabSheet tabsheet, Component tabContent) {
        tabsheet.removeComponent(tabContent);
    }

    public String getProperty(String s) {
        return super.getProperty(s);
    }
}