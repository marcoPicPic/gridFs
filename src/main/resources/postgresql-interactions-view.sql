-- View: public.interaction_fulltext

-- DROP VIEW public.interaction_fulltext;

CREATE OR REPLACE VIEW public.interaction_for_gridfs AS
 SELECT m.id,
    m.mail_subject,
    m.mail_date,
    m.mail_id,
    m.parsed_mail_id,
    m.file_name,
    m.attached_file_id,
    m.mail_body,
    m.tenant_id,
    m.thread_id,
    m.thread_priority,
    m.thread_desktop,
    m.thread_knowgroup_id,
    m.thread_language_id,
    m.thread_queue_id,
    m.indexft_modification_date,
    m.customer_email,
    m.operator_id,
    m.criteria_ids,
    m.notes,
        CASE
            WHEN max(doc.mail_id) IS NOT NULL THEN 1
            ELSE 0
        END AS vr_exist,
    string_agg(vr.operator_id::character varying::text, ','::text) AS vr_operator_ids,
    string_agg(vr.requester_operator_id::character varying::text, ','::text) AS vr_requester_ids,
    m.uuid_id
   FROM ( SELECT m_1.id,
            m_1.subject AS mail_subject,
            m_1.date_mail AS mail_date,
            m_1.id AS mail_id,
            "left"(pp.parsed_text_body, 20000) AS mail_body,
            v.knowdom_id AS tenant_id,
            th.id AS thread_id,
            th.priority AS thread_priority,
            th.desktop AS thread_desktop,
            th.ss_knowgroup_id AS thread_knowgroup_id,
            th.language_id AS thread_language_id,
            th.mailbox_id AS thread_queue_id,
            pp.parsed_mail_id AS parsed_mail_id,
            pa.file_name AS file_name,
            pa.id AS attached_file_id,
            th.indexft_modification_date,
            COALESCE(lower(m_1.contact_info::text), e.email::text) AS customer_email,
            th.operator_id,
            k.uuid_id,
            ( SELECT string_agg(mc.crit_id::character varying::text, ','::text) AS string_agg
                   FROM mails_criteria mc
                  WHERE mc.mail_id = m_1.id) AS criteria_ids,
            ( SELECT string_agg(postit.texte::text, ','::text) AS string_agg
                   FROM postit_threads postit
                  WHERE postit.thread_id = th.id) AS notes
           FROM mails m_1
             LEFT JOIN parsed_parts pp ON m_1.parsed_mail_id = pp.parsed_mail_id
             LEFT JOIN parsed_attachements pa ON m_1.parsed_mail_id = pa.parsed_mail_id
             LEFT JOIN threads th ON m_1.thread_id = th.id
             LEFT JOIN mailboxes mb ON th.mailbox_id = mb.id
             LEFT JOIN virtualdoms v ON v.id = mb.virtual_dom_id
             LEFT JOIN emails e ON th.client_email_id = e.id
             LEFT JOIN knowdoms k ON k.id = v.knowdom_id) m
     LEFT JOIN documents doc ON doc.mail_id = m.id AND (doc.deleted IS NULL OR doc.deleted = 0)
     LEFT JOIN voice_records vr ON vr.document_id = doc.id
  GROUP BY m.id, m.mail_subject, m.mail_id, m.attached_file_id, m.parsed_mail_id, m.file_name, m.mail_date, m.mail_body, m.tenant_id, m.thread_id, m.thread_priority, m.thread_desktop, m.thread_knowgroup_id, m.thread_language_id, m.thread_queue_id, m.indexft_modification_date, m.customer_email, m.operator_id, m.criteria_ids, m.notes, m.uuid_id;

ALTER TABLE public.interaction_for_gridfs
  OWNER TO akio;
GRANT ALL ON TABLE public.interaction_fulltext TO akio;
GRANT DELETE ON TABLE public.interaction_for_gridfs TO user6_delete;
GRANT INSERT ON TABLE public.interaction_for_gridfs TO user6_insert;
GRANT SELECT ON TABLE public.interaction_for_gridfs TO user6_select;
GRANT UPDATE ON TABLE public.interaction_for_gridfs TO user6_update;
