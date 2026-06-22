-- =======================================================================
-- V2: AÑADIR CONSTRAINTS, VALORES POR DEFECTO E ÍNDICES
-- =======================================================================

-- ---------------------------------------------------------
-- 1. VALORES POR DEFECTO (DEFAULTS)
-- ---------------------------------------------------------
ALTER TABLE users 
    ALTER COLUMN active SET DEFAULT TRUE,
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP,
    ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE project_categories 
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP,
    ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE projects 
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP,
    ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE project_members 
    ALTER COLUMN joined_at SET DEFAULT CURRENT_TIMESTAMP,
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP,
    ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE tasks 
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP,
    ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE comments 
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP,
    ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE activity_logs 
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP,
    ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP;

-- ---------------------------------------------------------
-- 2. FOREIGN KEYS (CLAVES FORÁNEAS)
-- ---------------------------------------------------------
ALTER TABLE projects 
    ADD CONSTRAINT fk_projects_category FOREIGN KEY (category_id) REFERENCES project_categories(id) ON DELETE RESTRICT;

ALTER TABLE project_members 
    ADD CONSTRAINT fk_pm_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_pm_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE tasks 
    ADD CONSTRAINT fk_tasks_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_tasks_user FOREIGN KEY (assigned_user_id) REFERENCES users(id) ON DELETE SET NULL;

ALTER TABLE comments 
    ADD CONSTRAINT fk_comments_author FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_comments_task FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE;

ALTER TABLE activity_logs 
    ADD CONSTRAINT fk_audit_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- ---------------------------------------------------------
-- 3. CHECKS Y CONSTRAINTS ÚNICOS
-- ---------------------------------------------------------
-- Evitar que un usuario se una dos veces al mismo proyecto
ALTER TABLE project_members 
    ADD CONSTRAINT uq_project_user UNIQUE (project_id, user_id);

-- Restringir los estados y prioridades a valores específicos
ALTER TABLE projects 
    ADD CONSTRAINT chk_projects_status CHECK (status IN ('PLANNED', 'IN_PROGRESS', 'ON_HOLD', 'COMPLETED', 'CANCELLED'));

ALTER TABLE tasks 
    ADD CONSTRAINT chk_tasks_status CHECK (status IN ('TODO', 'IN_PROGRESS', 'IN_REVIEW', 'DONE')),
    ADD CONSTRAINT chk_tasks_priority CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL'));

-- ---------------------------------------------------------
-- 4. ÍNDICES (INDEXES)
-- ---------------------------------------------------------
-- Índices para Foreign Keys (Mejoran los JOINs)
CREATE INDEX idx_projects_category_id ON projects(category_id);
CREATE INDEX idx_pm_user_id ON project_members(user_id);
CREATE INDEX idx_tasks_project_id ON tasks(project_id);
CREATE INDEX idx_tasks_assigned_user_id ON tasks(assigned_user_id);
CREATE INDEX idx_comments_task_id ON comments(task_id);
CREATE INDEX idx_comments_author_id ON comments(author_id);
CREATE INDEX idx_activity_logs_user_id ON activity_logs(user_id);

-- Índices para búsquedas y filtrados frecuentes
CREATE INDEX idx_projects_status ON projects(status);
CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_tasks_priority ON tasks(priority);

-- Índice compuesto polimórfico (Para los logs de auditoría)
CREATE INDEX idx_activity_logs_entity ON activity_logs(entity_type, entity_id);

-- Índice cronológico
CREATE INDEX idx_activity_logs_created_at ON activity_logs(created_at DESC);