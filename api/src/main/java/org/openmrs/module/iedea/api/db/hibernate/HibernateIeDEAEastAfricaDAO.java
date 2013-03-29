/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.iedea.api.db.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.openmrs.module.iedea.ImportLogItem;
import org.openmrs.module.iedea.api.db.IeDEAEastAfricaDAO;

/**
 * It is a default implementation of  {@link IeDEAEastAfricaDAO}.
 */
public class HibernateIeDEAEastAfricaDAO implements IeDEAEastAfricaDAO {
    protected final Log log = LogFactory.getLog(this.getClass());

    private SessionFactory sessionFactory;

    /**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public int getImportLogCount() {
        Long i = (Long) sessionFactory.getCurrentSession().createQuery(
                "select count(*) from ImportLogItem").uniqueResult();
        return i.intValue();
    }

    @Override
    public void saveOrUpdate(ImportLogItem item) {
        sessionFactory.getCurrentSession().saveOrUpdate(item);
    }

    @Override
    public List<ImportLogItem> getAllLogs() {
        Query q = sessionFactory.getCurrentSession().createQuery("select log from ImportLogItem log");
        return q.list();
    }

    @Override
    public ImportLogItem getLogItemByUUID(String uuid) {
        Query q = sessionFactory.getCurrentSession().createQuery("select log from ImportLogItem log where log.uuid = :uuid");
        q.setParameter("uuid", uuid);
        return (ImportLogItem) q.uniqueResult();
    }

    @Override
    public ImportLogItem getLotItemById(int id) {
        Query q = sessionFactory.getCurrentSession().createQuery("select log from ImportLogItem where log.id = :id");
        q.setParameter("id", id);
        return null;
    }
}